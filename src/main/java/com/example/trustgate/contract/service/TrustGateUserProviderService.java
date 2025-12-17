package com.example.trustgate.contract.service;

import com.example.trustgate.contract.dto.TrustGateUserDto;

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
}
