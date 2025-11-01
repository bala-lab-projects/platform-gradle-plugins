package io.github.platform.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for complete Spring Boot application configuration.
 *
 * This plugin provides production-ready Spring Boot setup including
 * - Spring Boot plugin with bootJar and bootRun tasks
 * - Spring Dependency Management for consistent versions
 * - Jackson JSR310 for Java 8+ date/time serialization
 * - MapStruct for type-safe object mapping
 * - Strict dependency resolution (fails on conflicts)
 * - All base Java and test conventions
 *
 * This plugin automatically applies:
 * - `io.github.platform.java-conventions` - Base Java setup
 * - `io.github.platform.spring-test-conventions` - Test dependencies
 * - `org.springframework.boot` - Spring Boot plugin
 * - `io.spring.dependency-management` - Dependency management
 *
 * **Usage:**
 * ```
 * plugins {
 *     id("io.github.platform.spring-conventions")
 * }
 *
 * dependencies {
 *     implementation("org.springframework.boot:spring-boot-starter-web")
 *     // Spring Boot manages versions - no need to specify
 * }
 * ```
 *
 * **Configured Dependencies: **
 * Dependency versions are managed in `gradle.properties` at the project root.
 *
 * **Strict Dependency Resolution: **
 * This plugin enforces strict dependency management to catch version conflicts early:
 * - `failOnVersionConflict()` - Build fails if dependencies have version conflicts
 * - `failOnDynamicVersions()` - Build fails on dynamic versions (e.g., "1.+", "latest.release")
 *
 * This ensures reproducible builds and prevents unexpected behavior from version mismatches.
 */
class SpringConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            applyRequiredPlugins()
            addCommonDependencies()
            configureStrictDependencyResolution()
        }
    }

    /** Applies Java conventions, Spring test conventions, Spring Boot, and dependency management plugins. */
    private fun Project.applyRequiredPlugins() {
        // Apply conventions in dependency order
        pluginManager.apply("io.github.platform.java-conventions")
        pluginManager.apply("io.github.platform.spring-test-conventions")

        // Apply Spring Boot plugins
        pluginManager.apply("org.springframework.boot")
        pluginManager.apply("io.spring.dependency-management")
    }

    /** Adds Jackson JSR310 and MapStruct dependencies. */
    private fun Project.addCommonDependencies() {
        val jacksonVersion = getVersionProperty("jacksonVersion")
        val mapstructVersion = getVersionProperty("mapstructVersion")

        dependencies {
            // Jackson JSR310 - Java 8 Date/Time API support for JSON
            // Required for proper serialization of LocalDate, LocalDateTime, Instant, etc.
            add("implementation", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

            // MapStruct - Compile-time bean mapping
            // Provides type-safe mapping between domain models and DTOs
            add("implementation", "org.mapstruct:mapstruct:$mapstructVersion")
            add("annotationProcessor", "org.mapstruct:mapstruct-processor:$mapstructVersion")

            // Include MapStruct processor for test code as well
            add("testAnnotationProcessor", "org.mapstruct:mapstruct-processor:$mapstructVersion")
        }
    }

    /** Enforces strict dependency resolution to fail on version conflicts and dynamic versions. */
    private fun Project.configureStrictDependencyResolution() {
        configurations.all {
            resolutionStrategy {
                // Fail fast on version conflicts
                // This prevents subtle bugs from using incompatible dependency versions
                failOnVersionConflict()

                // Fail on dynamic versions like "1.+" or "latest.release"
                // This ensures reproducible builds across environments
                failOnDynamicVersions()
            }
        }
    }

    /**
     * Retrieves a version property from gradle.properties.
     * Throws an exception with a helpful message if the property is not found.
     */
    private fun Project.getVersionProperty(propertyName: String): String =
        findProperty(propertyName) as String?
            ?: throw IllegalStateException(
                "Property '$propertyName' not found in gradle.properties. " +
                    "Please ensure gradle.properties exists in the project root with all required version properties.",
            )
}
