package com.example.trustgate.contract.service;

import com.example.trustgate.contract.dto.TrustGateRoleOption;
import com.example.trustgate.contract.dto.TrustGateUserDto;
import com.example.trustgate.contract.exception.TrustGateAuthenticationException;
import com.example.trustgate.contract.security.TrustGatePasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * TrustGate User Provider Service Contract
 *
 * This interface defines the contract that all user provider implementations must follow
 * when integrating with the TrustGate authorization server.
 *
 * Client implementations in the user folder or external systems must implement this
 * interface to provide user authentication data that conforms to TrustGate standards.
 *
 * This contract-based approach allows for:
 * - Clear separation between authorization server contracts and client implementations
 * - Flexibility in client implementation details while maintaining standard interfaces
 * - Easy testing through interface mocking
 * - Scalability to support multiple user provider implementations
 */
public interface TrustGateUserProviderService {

    /**
     * Find a user by username and return as TrustGate standard DTO
     *
     * @param username the username to search for
     * @return Optional containing TrustGateUserDto if user exists, empty otherwise
     */
    Optional<TrustGateUserDto> findUserByUsername(String username);

    /**
     * Optionally return a custom password encoder for this provider.
     *
     * <p>The authorization server resolves the user password encoder using a three-tier priority:</p>
     * <ol>
     *   <li><strong>This method</strong> — if non-null, the returned encoder takes highest priority.
     *       Use this for exotic hashing schemes not available in the admin settings dropdown.</li>
     *   <li><strong>Admin settings</strong> — hashing algorithm configured in the TrustGate
     *       admin settings table (supports BCrypt and PBKDF2).</li>
     *   <li><strong>BCrypt fallback</strong> — default {@code BCryptPasswordEncoder} if neither
     *       this method nor admin settings provide a configuration.</li>
     * </ol>
     *
     * <p>Most providers should <strong>not</strong> override this method. Configure the hashing
     * algorithm in the TrustGate admin settings instead. Only override if your user store uses
     * a hashing scheme not available in the admin dropdown.</p>
     *
     * @return a custom password encoder, or {@code null} to use the admin settings / BCrypt default
     */
    default TrustGatePasswordEncoder getPasswordEncoder() {
        return null;
    }

    /**
     * Optional post-authentication validation hook.
     *
     * <p>Called by the authorization server <strong>after</strong> the user's password has been
     * verified successfully but <strong>before</strong> the authentication result is finalized.
     * Implementations can throw a {@link TrustGateAuthenticationException} (or its subclass
     * {@link com.example.trustgate.contract.exception.UserAccountException}) to reject the
     * login with a domain-specific error code that the login page can display.</p>
     *
     * <p>Typical checks include user account status (inactive, terminated), role existence,
     * and organizational association (school/office active status).</p>
     *
     * <p>The default implementation is a no-op so existing providers are unaffected.</p>
     *
     * @param username the username that was authenticated
     * @throws TrustGateAuthenticationException if validation fails
     */
    default void validateAfterAuthentication(String username) throws TrustGateAuthenticationException {
        // no-op: clients override for custom post-password validation
    }

    /**
     * Check whether multi-factor authentication is required for this user.
     *
     * <p>Providers that support MFA should override this method to look up the user's
     * MFA enrollment status (e.g., from a database flag or external MFA service).
     * The authorization server calls this after password verification to decide
     * whether to initiate the OTP challenge flow.</p>
     *
     * <p>The default returns {@code false} so existing providers without MFA are unaffected.</p>
     *
     * @param username the authenticated username
     * @return {@code true} if the user requires MFA, {@code false} otherwise
     */
    default boolean isMfaEnabled(String username) {
        return false;
    }

    /**
     * Deliver a one-time password to the user via the provider's channel (SMS, email, etc.).
     *
     * <p>Called by the authorization server when MFA is required. The OTP code is generated
     * by the server; the provider is responsible only for delivery. Implementations
     * <strong>must</strong> throw a {@link TrustGateAuthenticationException} if delivery
     * fails — the authorization server uses this to block the login attempt.</p>
     *
     * <p>The default is a no-op so existing providers without MFA are unaffected.</p>
     *
     * @param username the authenticated username
     * @param otpCode  the one-time password to deliver
     * @throws TrustGateAuthenticationException if OTP delivery fails
     */
    default void sendOtp(String username, String otpCode) throws TrustGateAuthenticationException {
        // no-op: providers override to deliver OTP via SMS, email, etc.
    }

    /**
     * Check whether onboarding is required for this user.
     *
     * <p>Providers should override this to check if the user's status indicates
     * they need to complete onboarding (e.g., PENDING status, first login with
     * system-generated password).</p>
     *
     * <p>The default returns {@code false} so existing providers are unaffected.</p>
     *
     * @param username the authenticated username
     * @return {@code true} if the user needs onboarding, {@code false} otherwise
     */
    default boolean isOnboardingRequired(String username) {
        return false;
    }

    /**
     * Lifecycle hook called when a user completes the onboarding process.
     *
     * <p>Providers should override this to perform post-onboarding actions such as
     * activating a PENDING account, recording the onboarding timestamp, or
     * triggering downstream events.</p>
     *
     * <p>The default is a no-op so existing providers are unaffected.</p>
     *
     * @param username the username of the user who completed onboarding
     */
    default void onOnboardingCompleted(String username) {
        // no-op: providers override to handle onboarding completion
    }

    /**
     * Lifecycle hook called when a user successfully changes their password.
     *
     * <p>Providers should override this to persist the new password in their
     * user store. The password is already encoded by the authorization server
     * using the resolved password encoder.</p>
     *
     * <p>The default is a no-op so existing providers are unaffected.</p>
     *
     * @param username           the username of the user who changed their password
     * @param newEncodedPassword the new password, already encoded
     */
    default void onPasswordChangeCompleted(String username, String newEncodedPassword) {
        // no-op: providers override to persist the new password
    }

    /**
     * Determine whether the given user should be presented with
     * a multi-role selection step during login.
     *
     * <p>Called by the authorization server <strong>after</strong> confirming
     * the organization-level "Multiple Role Selection" setting is enabled.
     * If this method also returns {@code true}, the ROLE_SELECTION step
     * is added to the authentication pipeline.</p>
     *
     * <p>Providers that support multirole should override this to check
     * whether the user actually has more than one role. The default
     * returns {@code false} so existing providers are unaffected.</p>
     *
     * @param username the authenticated username
     * @return {@code true} if the user should see role selection, {@code false} otherwise
     */
    default boolean isMultiRoleEnabled(String username) {
        return false;
    }

    /**
     * Return the available roles for a user, for role selection during login.
     *
     * <p>Called by the authorization server when the "Multiple Role Selection"
     * setting is enabled and the user enters the role selection pipeline step.
     * Each {@link TrustGateRoleOption} carries generic display fields plus an
     * opaque {@code claims} map that will be embedded in the JWT when selected.</p>
     *
     * <p>Providers that support multirole should override this. The default
     * returns an empty list, meaning "no multirole support" — the role selection
     * step will auto-skip.</p>
     *
     * @param username the authenticated username
     * @return list of available roles, empty if multirole is not supported
     */
    default List<TrustGateRoleOption> getAvailableRoles(String username) {
        return Collections.emptyList();
    }

}