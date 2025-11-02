# Plugin Composition Architecture

This document visualizes how our Gradle convention plugins compose together to provide layered functionality.

## Plugin Dependency Graph

![Plugin Dependency Graph](diagrams/plugin-dependency-graph.png)

## Detailed Plugin Stack

![Detailed Plugin Stack](diagrams/plugin-stack-detailed.png)

## Plugin Responsibilities

```mermaid
mindmap
  root((Platform Plugins))
    Java Conventions
      Java 21 Toolchain
      Lombok Support
      Spotless Formatting
        google-java-format
        ktlint
      JaCoCo Coverage
      JUnit Platform
    Spring Test
      Spring Boot Test
      MockK
      Test Dependencies
    Spring Core
      Spring Boot
      Dependency Management
      MapStruct
      Strict Resolution
    Spring Web
      Web Starter
      Validation
      AOP
    Spring WebFlux
      WebFlux Starter
      Validation
      Reactor Test
```

## Build Lifecycle Flow

```mermaid
%%{init: {'theme':'dark'}%%
sequenceDiagram
    participant Dev as Developer
    participant Gradle as Gradle Build
    participant Java as JavaConventionsPlugin
    participant Spotless as Spotless Plugin
    participant Test as Test Tasks
    participant JaCoCo as JaCoCo Plugin
    Dev ->> Gradle: ./gradlew build
    Gradle ->> Java: Apply java-conventions
    Java ->> Spotless: Configure formatting
    Java ->> JaCoCo: Configure coverage
    Gradle ->> Spotless: spotlessCheck
    Spotless -->> Gradle: ✓ Formatting OK
    Gradle ->> Test: Run tests
    Test -->> JaCoCo: Collect coverage
    JaCoCo -->> Gradle: Generate reports
    Gradle -->> Dev: Build Success
    Gradle -->> Dev: Coverage: 85%
```

## Configuration Propagation

![Configuration Propagation](diagrams/configuration-propagation.png)

## Version Management Flow

```mermaid
stateDiagram-v2
    [*] --> gradle.properties
    gradle.properties --> generateVersions: Build time
    generateVersions --> GeneratedVersions.kt: Auto-generated
    GeneratedVersions.kt --> Plugin: Type-safe constants
    Plugin --> ConsumerBuild: Apply configuration
    ConsumerBuild --> [*]

    note right of gradle.properties
        Single source of truth
        spotlessVersion=8.0.0
        ktlintVersion=1.7.1
    end note

    note right of GeneratedVersions.kt
        const val SPOTLESS = "8.0.0"
        const val KTLINT = "1.7.1"
    end note
```

## Key Design Principles

### 1. Layered Composition
Plugins build on each other incrementally:
- **Base Layer**: Java tooling (java-conventions)
- **Framework Layer**: Spring Boot (spring-core-conventions)
- **Application Layer**: Web/WebFlux (specific implementations)

### 2. Single Responsibility
Each plugin has a focused purpose:
- `java-conventions`: Code quality + toolchain
- `spring-test-conventions`: Testing only
- `spring-core-conventions`: Spring Boot integration
- `spring-web-conventions`: Web-specific dependencies

### 3. Convention Over Configuration
Sensible defaults with escape hatches:
- Pre-configured but customizable
- Centralized version management
- Standard repository configuration

### 4. Fail-Fast Philosophy
Catch issues early:
- Spotless fails build on formatting issues
- Strict dependency resolution fails on conflicts
- JaCoCo generates reports automatically
