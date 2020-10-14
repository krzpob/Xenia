import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    id("com.google.cloud.tools.jib") version "2.5.0"
}



group = "pl.javasoft"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.github.debop:koda-time:2.0.0")
    implementation("joda-time:joda-time:2.10")
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.4.6")
    implementation(group="org.jadira.usertype", name= "usertype.core", version= "6.0.1.GA")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.10.4")
    implementation(group="com.fasterxml.jackson.datatype", name="jackson-datatype-joda")
    implementation(group= "org.postgresql", name= "postgresql", version="9.4.1212")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

jib {
    from {
        image = "adoptopenjdk/openjdk11:jre11u-alpine-nightly"
    }
    to {
        image = "krzpob/xenia-api"
        tags = setOf("$version","latest")
//        auth {
//            username = System.getenv("DOCKERHUB_USERNAME")
//            password = System.getenv("DOCKERHUB_PASSWORD")
//        }
    }
    container{
        ports = listOf("8080")
        jvmFlags = listOf("-Dserver.port=\$PORT")
        environment = mapOf(Pair("PORT","8080"))
    }
}


