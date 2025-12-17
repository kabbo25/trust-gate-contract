# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a contract module for the TrustGate Authorization Server. It defines standard interfaces and DTOs that client implementations must follow when integrating with the TrustGate authorization server.

**Key Architecture Points:**
- This is a contract-only library (no implementations)
- Provides `TrustGateUserProviderService` interface that clients must implement
- Defines `TrustGateUserDto` which directly implements Spring Security's `UserDetails` interface
- Designed for separation between authorization server contracts and client implementations

## Build Commands

```bash
# Clean and compile
mvn clean compile

# Package the contract JAR
mvn clean package

# Install to local Maven repository
mvn clean install
```

Note: The maven-compiler-plugin has `<skip>true</skip>` configured, so compilation is skipped by default.

## Project Structure

- `src/main/java/com/example/trustgate/contract/dto/` - Standard DTOs for TrustGate
  - `TrustGateUserDto.java` - User contract implementing Spring Security UserDetails
- `src/main/java/com/example/trustgate/contract/service/` - Service interfaces
  - `TrustGateUserProviderService.java` - Main contract interface for user provider implementations

## Technology Stack

- Java 21
- Spring Boot 3.4.4
- Spring Security Core (for UserDetails interface)
- Lombok (for reducing boilerplate)
- Maven

## Contract Implementation Guidelines

When client systems implement `TrustGateUserProviderService`:
- Must return `TrustGateUserDto` populated with all required UserDetails fields
- The `findUserByUsername` method returns `Optional<TrustGateUserDto>`
- Authorities should be provided as a collection of String values
- All account status flags (enabled, accountNonExpired, accountNonLocked, credentialsNonExpired) must be set appropriately
