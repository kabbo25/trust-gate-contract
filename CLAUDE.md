# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a contract module for the TrustGate Authorization Server. It defines standard interfaces and DTOs that client implementations must follow when integrating with the TrustGate authorization server.

**Maven Coordinates:** `io.github.kabbo25:trustgate-contract`
**Published to:** Maven Central via Central Portal

**Key Architecture Points:**
- This is a contract-only library (no implementations)
- Provides `TrustGateUserProviderService` interface that clients must implement
- Defines `TrustGateUserDto` as a framework-agnostic user DTO
- Designed for separation between authorization server contracts and client implementations

## Build Commands

```bash
# Clean and compile
mvn clean compile

# Package the contract JAR
mvn clean package

# Install to local Maven repository
mvn clean install

# Deploy to Maven Central (requires GPG key + ~/.m2/settings.xml credentials)
mvn clean deploy
```

## Project Structure

- `src/main/java/io/github/kabbo25/trustgate/contract/dto/` - Standard DTOs
  - `TrustGateUserDto.java` - User contract DTO
  - `TrustGateRoleOption.java` - Multi-role selection DTO
- `src/main/java/io/github/kabbo25/trustgate/contract/exception/` - Exceptions
  - `TrustGateAuthenticationException.java` - Post-auth validation exception
- `src/main/java/io/github/kabbo25/trustgate/contract/security/` - Security contracts
  - `TrustGatePasswordEncoder.java` - Custom password encoder interface
- `src/main/java/io/github/kabbo25/trustgate/contract/service/` - Service interfaces
  - `TrustGateUserProviderService.java` - Main contract interface
- `src/main/java/io/github/kabbo25/trustgate/contract/view/` - View contracts
  - `TrustGateViewProvider.java` - Custom template provider interface

## Technology Stack

- Java 21
- Lombok (only dependency)
- Maven with Central Publishing Plugin

## Contract Implementation Guidelines

When client systems implement `TrustGateUserProviderService`:
- Must return `TrustGateUserDto` populated with all required fields
- The `findUserByUsername` method returns `Optional<TrustGateUserDto>`
- Authorities should be provided as a collection of String values
- All account status flags (enabled, accountNonExpired, accountNonLocked, credentialsNonExpired) must be set appropriately
