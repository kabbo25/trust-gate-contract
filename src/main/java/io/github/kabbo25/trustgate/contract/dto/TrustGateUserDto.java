package io.github.kabbo25.trustgate.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Standard TrustGate User DTO Contract
 *
 * This DTO defines the contract that all user provider implementations must follow
 * when providing user authentication data to the TrustGate authorization server.
 *
 * This is a framework-agnostic DTO with no Spring dependencies, allowing implementations
 * to be used across different authentication frameworks and platforms.
 *
 * The authorization server is responsible for adapting this DTO to the specific
 * authentication framework requirements (e.g., Spring Security's UserDetails).
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustGateUserDto {

    /**
     * The unique username for authentication
     */
    private String username;

    /**
     * The encoded/hashed password for authentication
     */
    private String encodedPassword;

    /**
     * Whether the user account is enabled
     */
    private boolean enabled;

    /**
     * Whether the user account is not expired
     */
    private boolean accountNonExpired;

    /**
     * Whether the user account is not locked
     */
    private boolean accountNonLocked;

    /**
     * Whether the user credentials are not expired
     */
    private boolean credentialsNonExpired;

    /**
     * Collection of authority strings granted to the user
     * (e.g., "ROLE_USER", "ROLE_ADMIN")
     */
    private Collection<String> authorities;

    /**
     * Optional custom claims to be included in the JWT access token.
     * Providers can populate this map with domain-specific data (e.g., user roles,
     * profile information) that will be carried through to the JWT.
     * Defaults to an empty map if not set.
     */
    @Builder.Default
    private Map<String, Object> customClaims = Collections.emptyMap();
}
