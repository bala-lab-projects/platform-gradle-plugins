plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    kotlin("jvm")
}

group = "io.github.platform"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Spring Boot Gradle Plugin - latest stable
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.4.1")

    // Spring Dependency Management Plugin - latest stable
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
}

gradlePlugin {
    plugins {
        register("javaConventions") {
            id = "io.github.platform.java-conventions"
            implementationClass = "io.github.platform.gradle.JavaConventionsPlugin"
            displayName = "Java Conventions Plugin"
            description = "Provides base Java configuration with Lombok, Checkstyle (Google Style), and JaCoCo"
        }

        register("springTestConventions") {
            id = "io.github.platform.spring-test-conventions"
            implementationClass = "io.github.platform.gradle.SpringTestConventionsPlugin"
            displayName = "Spring Test Conventions Plugin"
            description = "Provides Spring Boot test dependencies and configuration"
        }

        register("springConventions") {
            id = "io.github.platform.spring-conventions"
            implementationClass = "io.github.platform.gradle.SpringConventionsPlugin"
            displayName = "Spring Boot Conventions Plugin"
            description = "Provides complete Spring Boot application configuration with MapStruct and strict dependency management"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}
kotlin {
    jvmToolchain(21)
}