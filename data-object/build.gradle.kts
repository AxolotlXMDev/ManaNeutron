plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
}

group = "github.axolotl"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.61")
}

tasks.test {
    useJUnitPlatform()
}
