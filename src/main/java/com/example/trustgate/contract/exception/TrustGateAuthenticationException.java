package com.example.trustgate.contract.exception;

import java.util.Map;

/**
 * Framework-agnostic base exception for post-authentication validation failures.
 *
 * Thrown by {@link com.example.trustgate.contract.service.TrustGateUserProviderService#validateAfterAuthentication}
 * when a domain-specific check fails after password verification (e.g., account inactive,
 * school not active, no roles).
 *
 * The authorization server bridges this into its own framework-specific exception
 * (e.g., Spring Security's AuthenticationException) so that the error code and message
 * are propagated to the login page.
 */
public class TrustGateAuthenticationException extends RuntimeException {

    private final String errorCode;
    private final Map<String, String> localizedMessages;

    public TrustGateAuthenticationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.localizedMessages = null;
    }

    /**
     * Creates an exception with localized messages for multiple languages.
     *
     * @param errorCode          machine-readable error code (e.g. "account_terminated")
     * @param defaultMessage     fallback message when no localized variant matches
     * @param localizedMessages  language-code → message map (e.g. "en" → "...", "bn" → "...")
     */
    public TrustGateAuthenticationException(String errorCode, String defaultMessage,
                                            Map<String, String> localizedMessages) {
        super(defaultMessage);
        this.errorCode = errorCode;
        this.localizedMessages = localizedMessages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getLocalizedMessages() {
        return localizedMessages;
    }

    /**
     * Returns the message for the given language code, falling back to {@link #getMessage()}.
     *
     * @param languageCode BCP-47 language tag (e.g. "en", "bn")
     * @return localized message or the default message
     */
    public String getLocalizedMessage(String languageCode) {
        if (localizedMessages != null && languageCode != null) {
            String msg = localizedMessages.get(languageCode);
            if (msg != null) {
                return msg;
            }
        }
        return getMessage();
    }
}
