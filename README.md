# TrustGate Contract

TrustGate Authorization Server Contract Module - A contract-only library that defines standard interfaces and DTOs for TrustGate authorization server integrations.

## Overview

This is a contract-only library (no implementations) that provides:
- `TrustGateUserProviderService` interface for client implementations
- `TrustGateUserDto` which implements Spring Security's `UserDetails` interface
- Designed for separation between authorization server contracts and client implementations

## Technology Stack

- Java 21
- Spring Boot 3.4.4
- Spring Security Core
- Lombok
- Maven

## Using this Package

### Adding as a Dependency

To use this package in your project, add it as a Maven dependency:

```xml
<dependency>
    <groupId>com.example.trustgate</groupId>
    <artifactId>trustgate-contract</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configuring GitHub Packages Authentication

Since this package is hosted on GitHub Packages, you need to configure authentication in your `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          https://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_PERSONAL_ACCESS_TOKEN</password>
    </server>
  </servers>
</settings>
```

**Creating a Personal Access Token:**
1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Select the `read:packages` scope
4. Copy the generated token and use it as the password in settings.xml

### Adding the Repository

Add the GitHub Packages repository to your project's `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/kabbo25/trust-gate-contract</url>
    </repository>
</repositories>
```

## Implementation Guidelines

When implementing `TrustGateUserProviderService` in your client systems:

- Must return `TrustGateUserDto` populated with all required UserDetails fields
- The `findUserByUsername` method returns `Optional<TrustGateUserDto>`
- Authorities should be provided as a collection of String values
- All account status flags must be set:
  - `enabled`
  - `accountNonExpired`
  - `accountNonLocked`
  - `credentialsNonExpired`

## Building Locally

```bash
# Clean and compile
mvn clean compile

# Package the contract JAR
mvn clean package

# Install to local Maven repository
mvn clean install
```

## Publishing (Maintainers Only)

This package is automatically published to GitHub Packages when a new release is created. To publish:

1. Update the version in `pom.xml`
2. Commit and push changes
3. Create a new release on GitHub
4. The GitHub Actions workflow will automatically publish to GitHub Packages

You can also manually trigger the publish workflow from the Actions tab.

## License

[Add your license here]
