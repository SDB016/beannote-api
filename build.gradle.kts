import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.25"

    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
}

group = "com.beannote"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

/**
 * https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support
 * automatically supported annotation
 * @Component, @Async, @Transactional, @Cacheable, @SpringBootTest,
 * @Configuration, @Controller, @RestController, @Service, @Repository.
 * jpa meta-annotations not automatically opened through the default settings of the plugin.spring
 */
allOpen {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    /** spring boot starter **/
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    /** kotlin **/
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    /** logger */
    implementation("io.github.oshai:kotlin-logging-jvm:${DependencyVersion.KOTLIN_LOGGING}")
    implementation("net.logstash.logback:logstash-logback-encoder:${DependencyVersion.LOGBACK_ENCODER}")

    /** jwt **/
    implementation("io.jsonwebtoken:jjwt-api:${DependencyVersion.JWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${DependencyVersion.JWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${DependencyVersion.JWT_VERSION}")

    /** spring integration **/
    implementation("org.springframework.integration:spring-integration-mongodb")
    implementation("org.springframework.integration:spring-integration-webflux")
//    implementation("org.springframework.integration:spring-integration-jpa")

    /** configuration processor **/
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    /** test **/
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.integration:spring-integration-test")

    /** kotest **/
    testImplementation("io.kotest:kotest-runner-junit5:${DependencyVersion.KOTEST}")
    testImplementation("io.kotest:kotest-assertions-core:${DependencyVersion.KOTEST}")
    testImplementation("io.kotest:kotest-property:${DependencyVersion.KOTEST}")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:${DependencyVersion.KOTEST_EXT_TESTCONTAINER}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${DependencyVersion.KOTEST_EXT_SPRING}")

    /** mockk **/
    testImplementation("io.mockk:mockk:${DependencyVersion.MOCKK}")

}

// defaultTasks("bootRun")

springBoot.buildInfo { properties { } }

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.7"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
