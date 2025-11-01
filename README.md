# Platform Gradle Plugins

**Production-Ready Gradle Convention Plugins for Java & Spring Boot Applications**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Gradle](https://img.shields.io/badge/Gradle-8.14-blue.svg)](https://gradle.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

Reusable Gradle convention plugins that enforce best practices, standardize build configurations, and provide production-ready defaults for Java and Spring Boot projects.

---

## Overview

These plugins eliminate boilerplate build configuration and enforce consistent standards across projects by providing:

- **Java 21** toolchain configuration
- **Google Java Style Guide** enforcement via Checkstyle
- **JaCoCo code coverage** with comprehensive reporting
- **Lombok** for reducing boilerplate
- **MapStruct** for type-safe object mapping
- **Spring Boot 3.4** with dependency management
- **Strict dependency resolution** (fails on conflicts)
- **Comprehensive testing setup** (JUnit 5, MockK, Spring Test)

---

## Quick Start

### **1. Publish to Maven Local**

```bash
git clone https://github.com/bala-lab-projects/platform-gradle-plugins.git
cd platform-gradle-plugins

# Build and publish
./gradlew publishToMavenLocal
```

### **2. Use in Your Project**

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "my-spring-app"
```

```kotlin
// build.gradle.kts
plugins {
    id("io.github.platform.spring-conventions") version "1.0.0"
}

group = "com.example"
version = "1.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Spring Boot manages versions - no need to specify!
}
```

**That's it!** You now have:
- Java 21 toolchain
- Lombok support
- Google Java Style checking
- JaCoCo code coverage
- Spring Boot 3.4.1
- MapStruct mapping
- Comprehensive test setup
- Strict dependency management

---

## Available Plugins

### **Plugin 1: `io.github.platform.java-conventions`**

Foundation plugin that provides base Java configuration.

**What it configures:**
- ✅ Java 21 toolchain
- ✅ Lombok (compile + annotation processing)
- ✅ Google Java Style Guide via Checkstyle
- ✅ JaCoCo code coverage (XML + HTML + CSV)
- ✅ JUnit Platform for testing
- ✅ Maven Central + Maven Local repositories

**Usage:**
```kotlin
plugins {
    id("io.github.platform.java-conventions")
}
```

**Versions:**
- Lombok: `1.18.36`
- Checkstyle: `10.20.2`
- JaCoCo: `0.8.12`

---

### **Plugin 2: `io.github.platform.spring-test-conventions`**

Adds comprehensive testing support for Spring Boot applications.

**What it configures:**
- ✅ Applies `java-conventions` automatically
- ✅ Spring Boot Starter Test (includes JUnit 5, Mockito, AssertJ)
- ✅ MockK for powerful mocking
- ✅ JUnit Platform Launcher
- ✅ Excludes conflicting logging implementations

**Usage:**
```kotlin
plugins {
    id("io.github.platform.spring-test-conventions")
}
```

**Versions:**
- Spring Boot: `3.4.1`
- MockK: `1.13.14`

---

### **Plugin 3: `io.github.platform.spring-conventions`**

Complete Spring Boot application setup with production-ready defaults.

**What it configures:**
- ✅ Applies both `java-conventions` and `spring-test-conventions`
- ✅ Spring Boot plugin (`bootJar`, `bootRun` tasks)
- ✅ Spring Dependency Management (consistent versions)
- ✅ Jackson JSR310 (Java 8+ date/time support)
- ✅ MapStruct (type-safe object mapping)
- ✅ Strict dependency resolution (fails on conflicts & dynamic versions)

**Usage:**
```kotlin
plugins {
    id("io.github.platform.spring-conventions")
}
```

**Versions:**
- Spring Boot: `3.4.1`
- Jackson: `2.18.2`
- MapStruct: `1.6.3`
- Spring Dependency Management: `1.1.7`

---

## Plugin Composition

```
io.github.platform.spring-conventions
    │
    ├──> io.github.platform.java-conventions
    │       ├─> java-library
    │       ├─> checkstyle (Google Java Style)
    │       ├─> jacoco
    │       ├─> Java 21 toolchain
    │       ├─> Lombok
    │       └─> JUnit Platform
    │
    ├──> io.github.platform.spring-test-conventions
    │       ├─> Spring Boot Starter Test
    │       ├─> MockK
    │       └─> JUnit Platform Launcher
    │
    ├──> org.springframework.boot
    ├──> io.spring.dependency-management
    ├──> Jackson JSR310
    ├──> MapStruct
    └──> Strict dependency resolution
```

---

## Code Quality: Google Java Style

This plugin enforces the **Google Java Style Guide** via Checkstyle with **zero tolerance** for violations.

**Key rules enforced:**
- 2-space indentation
- No wildcard imports
- Consistent naming conventions
- Proper Javadoc formatting
- Line length limits (100 characters)
- Proper whitespace usage

**Configuration:**
```kotlin
checkstyle {
    toolVersion = "10.20.2"
    config = resources.text.fromUri(
        "https://raw.githubusercontent.com/checkstyle/checkstyle/checkstyle-10.20.2/src/main/resources/google_checks.xml"
    )
    maxWarnings = 0  // Fail on any warning
    maxErrors = 0    // Fail on any error
}
```

**Run checkstyle:**
```bash
./gradlew check
```

---

## Code Coverage: JaCoCo

Automatic code coverage reporting after every test run.

**What you get:**
- XML report (for CI/CD tools)
- HTML report (human-readable)
- CSV report (for data analysis)

**Run tests and generate coverage:**
```bash
./gradlew test

# Coverage report location:
# build/reports/jacoco/test/html/index.html
```

**Customize coverage thresholds (in your project):**
```kotlin
tasks.named<JacocoReport>("jacocoTestReport") {
    doLast {
        val report = reports.xml.outputLocation.get().asFile
        // Add custom verification logic
    }
}
```

---

## Strict Dependency Management

The `spring-conventions` plugin enforces strict dependency resolution to prevent version conflicts and ensure reproducible builds.

**What's enforced:**
```kotlin
configurations.all {
    resolutionStrategy {
        failOnVersionConflict()    // Fail if dependencies request different versions
        failOnDynamicVersions()    // Fail on "1.+", "latest.release", etc.
    }
}
```

**Why this matters:**
- Catches dependency conflicts **at build time** (not runtime)
- Ensures **reproducible builds** across environments
- Prevents unexpected behavior from version mismatches
- Forces explicit dependency management decisions

**Resolving conflicts:**
```kotlin
// In your project's build.gradle.kts
configurations.all {
    resolutionStrategy {
        force("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    }
}
```

---

## Testing Setup

**What's included:**

| Library | Purpose |
|---------|---------|
| JUnit 5 (Jupiter) | Modern testing framework |
| Spring Boot Test | Spring-specific test utilities |
| Spring Test | Core Spring testing support |
| Mockito | Traditional Java mocking |
| MockK | Kotlin-style mocking (works with Java) |
| AssertJ | Fluent assertions |
| Hamcrest | Matcher library |
| JSONassert | JSON assertion library |
| JsonPath | JSON path expressions |

**Example test:**
```java
@SpringBootTest
class OrderServiceTest {
    
    @Autowired
    private OrderService orderService;
    
    @Test
    void shouldCreateOrder() {
        // Given
        OrderRequest request = OrderRequest.builder()
            .customerId("CUST-123")
            .build();
        
        // When
        Order order = orderService.createOrder(request);
        
        // Then
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }
}
```

---

## MapStruct Integration

MapStruct is automatically configured for type-safe object mapping between domain models and DTOs.

**Example mapper:**
```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    OrderResponse toResponse(Order order);
    
    Order toDomain(OrderRequest request);
    
    @Mapping(target = "id", ignore = true)
    void updateOrderFromRequest(OrderRequest request, @MappingTarget Order order);
}
```

**No additional configuration needed!** MapStruct annotation processor is already set up.

---

## Example Projects

### **Minimal Spring Boot REST API**

```kotlin
// build.gradle.kts
plugins {
    id("io.github.platform.spring-conventions") version "1.0.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
}
```

### **Library/Module (No Spring Boot)**

```kotlin
// build.gradle.kts
plugins {
    id("io.github.platform.java-conventions") version "1.0.0"
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.apache.commons:commons-lang3:3.14.0")
}
```

---

## Commands Reference

```bash
# Build plugins
./gradlew build

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean

# Publish to Maven Local
./gradlew publishToMavenLocal

# Check dependency versions
make check-versions
```

---

## Architecture Decisions

### **Why Convention Plugins?**
- Eliminates copy-paste build configuration across projects
- Enforces consistent standards organization-wide
- Updates propagate automatically when plugins are updated
- Reduces cognitive load for developers

### **Why Google Java Style?**
- Industry-standard, well-documented
- Supported by major IDEs
- Enforces readability and consistency
- Prevents bikeshedding over formatting

### **Why Strict Dependency Resolution?**
- Catches issues at build time (not runtime)
- Forces explicit dependency management
- Ensures reproducible builds
- Prevents dependency hell

### **Why MapStruct over Reflection?**
- Compile-time safety (errors caught early)
- Better performance (no reflection)
- IDE support (autocomplete, refactoring)
- Clear, debuggable generated code

---

## License

MIT License - see [LICENSE](LICENSE)

---

## Author

**Balamurugan Elangovan**  
Principal Software Engineer | Platform Engineering

[GitHub](https://github.com/bala-lab-projects) | [LinkedIn](https://www.linkedin.com/in/balamurugan-elangovan-53791985/) | mail.bala0224@gmail.com

---

## Contributing

This is a personal portfolio project, but feedback and suggestions are welcome! Please open an issue to discuss potential changes.

---
