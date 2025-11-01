package io.github.platform.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.*
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Convention plugin that provides base Java configuration for all projects.
 *
 * <p>This plugin establishes foundational build standards including
 * <ul>
 *   <li>Java 21 toolchain configuration</li>
 *   <li>Lombok support for reducing boilerplate code</li>
 *   <li>Google Java Style Guide enforcement via Checkstyle</li>
 *   <li>JaCoCo code coverage reporting (XML, HTML, CSV)</li>
 *   <li>JUnit Platform for modern testing</li>
 *   <li>Standard repository configuration</li>
 * </ul>
 *
 * <p><b>Usage:</b>
 * <pre>
 * plugins {
 *     id("io.github.platform.java-conventions")
 * }
 * </pre>
 *
 * </p>
 */
class JavaConventionsPlugin : Plugin<Project> {

    companion object {
        private const val LOMBOK_VERSION = "1.18.36"
        private const val CHECKSTYLE_VERSION = "10.20.2"
        private const val JACOCO_VERSION = "0.8.12"
    }

    override fun apply(project: Project) {
        with(project) {
            applyPlugins()
            configureJavaToolchain()
            configureRepositories()
            configureLombok()
            configureCheckstyle()
            configureJacoco()
            configureTesting()
        }
    }

    /** Applies java-library, checkstyle, and jacoco plugins. */
    private fun Project.applyPlugins() {
        pluginManager.apply("java-library")
        pluginManager.apply("checkstyle")
        pluginManager.apply("jacoco")
    }

    /** Configures Java 21 toolchain. */
    private fun Project.configureJavaToolchain() {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }
    }

    /** Adds Maven Central and Maven Local repositories. */
    private fun Project.configureRepositories() {
        repositories {
            mavenCentral()
            mavenLocal()
        }
    }

    /** Adds Lombok dependencies for both main and test source sets. */
    private fun Project.configureLombok() {
        dependencies {
            add("compileOnly", "org.projectlombok:lombok:$LOMBOK_VERSION")
            add("annotationProcessor", "org.projectlombok:lombok:$LOMBOK_VERSION")
            add("testCompileOnly", "org.projectlombok:lombok:$LOMBOK_VERSION")
            add("testAnnotationProcessor", "org.projectlombok:lombok:$LOMBOK_VERSION")
        }
    }

    /** Configures Checkstyle with Google Java Style Guide and strict enforcement. */
    private fun Project.configureCheckstyle() {
        extensions.configure<CheckstyleExtension> {
            toolVersion = CHECKSTYLE_VERSION

            // Enforce Google Java Style Guide
            config = resources.text.fromUri(
                "https://raw.githubusercontent.com/checkstyle/checkstyle/checkstyle-${CHECKSTYLE_VERSION}/src/main/resources/google_checks.xml"
            )

            // Strict enforcement - fail on any violation
            maxWarnings = 0
            maxErrors = 0

            // Apply to both main and test source sets
            isShowViolations = true
        }

        // Configure checkstyle tasks
        tasks.withType<Checkstyle>().configureEach {
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }
    }

    /** Configures JaCoCo code coverage with XML, HTML, and CSV reports. */
    private fun Project.configureJacoco() {
        extensions.configure<JacocoPluginExtension> {
            toolVersion = JACOCO_VERSION
        }

        tasks.named<JacocoReport>("jacocoTestReport") {
            dependsOn(tasks.withType<Test>())

            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(true)
            }

            // Configure coverage thresholds (optional - can be overridden in projects)
            doLast {
                logger.lifecycle("Code coverage report generated: ${reports.html.outputLocation.get()}/index.html")
            }
        }
    }

    /** Configures test tasks with JUnit Platform, parallel execution, and coverage reporting. */
    private fun Project.configureTesting() {
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()

            // Run tests with parallel execution
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

            // Always generate coverage report after tests
            finalizedBy(tasks.named("jacocoTestReport"))

            // Configure test logging
            testLogging {
                events("passed", "skipped", "failed")
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                showStandardStreams = false
            }
        }
    }
}