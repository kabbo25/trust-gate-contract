package com.example.trustgate.contract.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Standard TrustGate User DTO Contract
 *
 * This DTO defines the contract that all user provider implementations must follow
 * when providing user authentication data to the TrustGate authorization server.
 *
 * This contract ensures consistency and standardization across different user
 * authentication backends while maintaining the TrustGate branding and architecture.
 *
 * Implements UserDetails directly to eliminate the need for conversion between DTOs.
 */
@Getter
@Setter
@Builder
public class TrustGateUserDto implements UserDetails {

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
     */
    private Collection<String> authorities;

    // UserDetails interface implementation

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
