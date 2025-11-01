package io.github.platform.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for complete Spring Boot application configuration.
 *
 * <p>This plugin provides production-ready Spring Boot setup including:
 * <ul>
 *   <li>Spring Boot plugin (3.4.1) with bootJar and bootRun tasks</li>
 *   <li>Spring Dependency Management for consistent versions</li>
 *   <li>Jackson JSR310 for Java 8+ date/time serialization</li>
 *   <li>MapStruct for type-safe object mapping</li>
 *   <li>Strict dependency resolution (fails on conflicts)</li>
 *   <li>All base Java and test conventions</li>
 * </ul>
 *
 * <p>This plugin automatically applies:
 * <ul>
 *   <li>{@code io.github.platform.java-conventions} - Base Java setup</li>
 *   <li>{@code io.github.platform.spring-test-conventions} - Test dependencies</li>
 *   <li>{@code org.springframework.boot} - Spring Boot plugin</li>
 *   <li>{@code io.spring.dependency-management} - Dependency management</li>
 * </ul>
 *
 * <p><b>Usage:</b>
 * <pre>
 * plugins {
 *     id("io.github.platform.spring-conventions")
 * }
 *
 * dependencies {
 *     implementation("org.springframework.boot:spring-boot-starter-web")
 *     // Spring Boot manages versions - no need to specify
 * }
 * </pre>
 *
 * <p><b>Dependency Versions:</b>
 * <ul>
 *   <li>Spring Boot: 3.4.1 (latest stable)</li>
 *   <li>Jackson: 2.18.2 (latest stable)</li>
 *   <li>MapStruct: 1.6.3 (latest stable)</li>
 * </ul>
 *
 * <p><b>Strict Dependency Resolution:</b><br>
 * This plugin enforces strict dependency management to catch version conflicts early:
 * <ul>
 *   <li>{@code failOnVersionConflict()} - Build fails if dependencies have version conflicts</li>
 *   <li>{@code failOnDynamicVersions()} - Build fails on dynamic versions (e.g., "1.+", "latest.release")</li>
 * </ul>
 *
 * <p>This ensures reproducible builds and prevents unexpected behavior from version mismatches.
 */
class SpringConventionsPlugin : Plugin<Project> {

    companion object {
        private const val JACKSON_VERSION = "2.18.2"
        private const val MAPSTRUCT_VERSION = "1.6.3"
    }

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
        dependencies {
            // Jackson JSR310 - Java 8 Date/Time API support for JSON
            // Required for proper serialization of LocalDate, LocalDateTime, Instant, etc.
            add("implementation", "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$JACKSON_VERSION")

            // MapStruct - Compile-time bean mapping
            // Provides type-safe mapping between domain models and DTOs
            add("implementation", "org.mapstruct:mapstruct:$MAPSTRUCT_VERSION")
            add("annotationProcessor", "org.mapstruct:mapstruct-processor:$MAPSTRUCT_VERSION")

            // Include MapStruct processor for test code as well
            add("testAnnotationProcessor", "org.mapstruct:mapstruct-processor:$MAPSTRUCT_VERSION")
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
}