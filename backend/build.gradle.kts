plugins {
    id("java")
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.6"
}

group = "github.axolotl"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<JavaExec>("bootRun") {
    workingDir = rootProject.file("test/${project.name}")
    doFirst {
        if (!workingDir.exists()) workingDir.mkdirs()
    }
}

tasks.test {
    useJUnitPlatform()
    workingDir = rootProject.file("test/junit-${project.name}")
    doFirst {
        workingDir.deleteRecursively()
        if (!workingDir.exists()) workingDir.mkdirs()
        val tokenFile = workingDir.resolve("config/token.txt")
        if (!tokenFile.exists()) {
            tokenFile.parentFile.mkdirs()
            tokenFile.writeText("test-token")
        }
    }
}
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":data-object"))
    implementation("com.github.42784:EasyUtil:-SNAPSHOT")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.61")

}

tasks.test {
    useJUnitPlatform()
    workingDir = rootProject.file("test/$name")
}