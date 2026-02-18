package com.example.trustgate.contract.view;

import java.util.Collections;
import java.util.Set;

/**
 * TrustGate View Provider Contract
 *
 * Defines which authentication pages a client provides custom Thymeleaf templates for.
 * Each method corresponds to one customizable page. Return non-null to override that
 * page with a custom template from the client JAR's classpath. Return null (the default)
 * to use the SSO server's built-in template.
 *
 * The returned string is the classpath-relative template path (without prefix/suffix).
 * Convention: templates reside at {@code classpath:/templates/{returnedValue}.html}
 * inside the client's user-provider JAR.
 *
 * Example: returning "custom/login" resolves to classpath:/templates/custom/login.html
 *
 * This interface follows the same default-method pattern as TrustGateUserProviderService:
 * all methods have defaults, so existing providers are unaffected.
 */
public interface TrustGateViewProvider {

    /**
     * Custom login page template path, or null to use the default.
     *
     * Model contract (frozen — SSO guarantees these variables):
     * - ${param.error}              : present if login failed
     * - ${param.logout}             : present if user logged out
     * - ${param.onboarded}          : present if onboarding completed
     * - ${isForgotPasswordEnabled}  : boolean
     * - ${forgotPasswordUrl}        : String
     * - ${providerErrorMessage}     : String (provider-specific error, nullable)
     * - #{login.*}                  : i18n messages
     *
     * Required form: POST th:action="@{/login}" with fields: username, password
     */
    default String getLoginTemplateName() {
        return null;
    }

    /**
     * Custom OTP page template path, or null to use the default.
     *
     * Model contract:
     * - ${username}          : String
     * - ${emailEnabled}      : boolean
     * - ${otherEnabled}      : boolean
     * - ${mfaMethodsCount}   : int
     * - ${otpSent}           : boolean
     * - ${remainingSeconds}  : long
     * - ${lastChannel}       : String ("EMAIL" or "PROVIDER")
     * - ${successMessage}    : String (nullable)
     * - ${error}             : String (nullable)
     *
     * Required form: POST th:action="@{/step/otp}" with fields: code, action, channel
     */
    default String getOtpTemplateName() {
        return null;
    }

    /**
     * Custom onboarding page template path, or null to use the default.
     *
     * Model contract:
     * - ${username}   : String
     *
     * Required form: POST th:action="@{/step/password-reset}" with fields: newPassword, confirmPassword
     */
    default String getOnboardingTemplateName() {
        return null;
    }

    /**
     * Custom role selection page template path, or null to use the default.
     *
     * Model contract:
     * - ${roles}          : List of TrustGateRoleOption (id, displayName, displayNameLocal,
     *                       description, descriptionLocal, primaryRole, tags, tagsLocal)
     * - ${username}       : String
     * - ${currentRoleId}  : String (nullable, for role-switch highlighting)
     *
     * Required form: POST th:action="@{/step/role-selection}" with field: selectedRoleId
     */
    default String getRoleSelectionTemplateName() {
        return null;
    }

    /**
     * Custom captcha page template path, or null to use the default.
     *
     * Model contract:
     * - ${username}      : String
     * - ${captchaImage}  : String (base64-encoded PNG)
     * - ${error}         : String (nullable)
     *
     * Required forms:
     * 1. Refresh: POST th:action="@{/step/CAPTCHA}" with hidden field action="refresh"
     * 2. Verify:  POST th:action="@{/step/CAPTCHA}" with hidden field action="verify" and field: answer
     */
    default String getCaptchaTemplateName() {
        return null;
    }

    /**
     * Custom password reset page template path, or null to use the default.
     *
     * Model contract:
     * - ${username}  : String
     * - ${error}     : String (nullable)
     *
     * Required form: POST th:action="@{/step/PASSWORD_RESET}" with hidden field action="verify"
     *                and fields: newPassword, confirmPassword
     */
    default String getPasswordResetTemplateName() {
        return null;
    }

    /**
     * Base path prefix for client static assets (CSS, JS, images) on the classpath.
     * If non-null, SSO registers a resource handler: /{prefix}/** → classpath:/static/{prefix}/
     *
     * Example: returning "custom" serves classpath:/static/custom/css/theme.css at /custom/css/theme.css
     *
     * @return static resource prefix, or null if client has no custom assets
     */
    default String getStaticResourcePrefix() {
        return null;
    }

    /**
     * Locales supported by the custom templates.
     * Used by startup validation to test templates in each locale.
     *
     * @return set of BCP-47 language codes (default: English only)
     */
    default Set<String> getSupportedLocales() {
        return Collections.singleton("en");
    }
}
