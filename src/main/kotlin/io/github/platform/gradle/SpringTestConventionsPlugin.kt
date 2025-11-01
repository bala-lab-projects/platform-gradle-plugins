package io.github.platform.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin that adds Spring Boot test dependencies and configuration.
 *
 * <p>This plugin provides a comprehensive testing setup for Spring Boot applications including:
 * <ul>
 *   <li>Spring Boot Starter Test (includes JUnit 5, Mockito, AssertJ, etc.)</li>
 *   <li>JUnit Platform Launcher for IDE integration</li>
 *   <li>MockK for Kotlin-style mocking (works with Java too)</li>
 *   <li>Proper logging exclusions to avoid conflicts</li>
 * </ul>
 *
 * <p>This plugin automatically applies {@code io.github.platform.java-conventions} to inherit
 * base Java configuration including JUnit Platform setup.
 *
 * <p><b>Usage:</b>
 * <pre>
 * plugins {
 *     id("io.github.platform.spring-test-conventions")
 * }
 * </pre>
 *
 * <p><b>Dependency Versions:</b>
 * <ul>
 *   <li>Spring Boot: 3.4.1 (latest stable)</li>
 *   <li>MockK: 1.13.14 (latest stable)</li>
 * </ul>
 *
 * <p><b>What's Included in spring-boot-starter-test:</b>
 * <ul>
 *   <li>JUnit 5 (Jupiter)</li>
 *   <li>Spring Test & Spring Boot Test</li>
 *   <li>AssertJ - fluent assertion library</li>
 *   <li>Hamcrest - matcher library</li>
 *   <li>Mockito - mocking framework</li>
 *   <li>JSONassert - JSON assertion library</li>
 *   <li>JsonPath - JSON path expressions</li>
 * </ul>
 */
class SpringTestConventionsPlugin : Plugin<Project> {

    companion object {
        private const val SPRING_BOOT_VERSION = "3.4.1"
        private const val MOCKK_VERSION = "1.13.14"
    }

    override fun apply(project: Project) {
        with(project) {
            applyBaseConventions()
            addTestDependencies()
            excludeConflictingDependencies()
        }
    }

    /** Applies Java conventions plugin for base configuration. */
    private fun Project.applyBaseConventions() {
        // Apply Java conventions first to get base configuration
        pluginManager.apply("io.github.platform.java-conventions")
    }

    /** Adds Spring Boot Starter Test, MockK, and JUnit Platform Launcher dependencies. */
    private fun Project.addTestDependencies() {
        dependencies {
            // Spring Boot Starter Test - comprehensive testing support
            add("testImplementation", "org.springframework.boot:spring-boot-starter-test:$SPRING_BOOT_VERSION")

            // MockK - powerful mocking library for Kotlin and Java
            add("testImplementation", "io.mockk:mockk:$MOCKK_VERSION")

            // JUnit Platform Launcher - required for IDE integration
            add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        }
    }

    /** Excludes conflicting logging dependencies to avoid conflicts with Log4j2. */
    private fun Project.excludeConflictingDependencies() {
        // Exclude logging implementations that conflict with Log4j2
        // This is important when projects use spring-boot-starter-log4j2
        configurations.all {
            exclude(mapOf("group" to "ch.qos.logback", "module" to "logback-classic"))
            exclude(mapOf("group" to "org.apache.logging.log4j", "module" to "log4j-to-slf4j"))
            exclude(mapOf("module" to "spring-boot-starter-logging"))
        }
    }
}