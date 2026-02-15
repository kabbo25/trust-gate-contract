package com.example.trustgate.contract.security;

/**
 * Framework-agnostic password encoder contract for TrustGate providers.
 *
 * Providers that use a non-standard password hashing scheme (e.g., PBKDF2, Argon2)
 * should implement this interface and return it from
 * {@link com.example.trustgate.contract.service.TrustGateUserProviderService#getPasswordEncoder()}.
 *
 * When no custom encoder is supplied, the auth server defaults to BCrypt.
 */
public interface TrustGatePasswordEncoder {

    /**
     * Encode the raw password.
     *
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    String encode(CharSequence rawPassword);

    /**
     * Verify a raw password against an encoded password.
     *
     * @param rawPassword     the raw password to check
     * @param encodedPassword the previously encoded password to compare against
     * @return true if the raw password matches the encoded password
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
