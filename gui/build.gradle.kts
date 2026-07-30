plugins {
    java
    application
    id("io.freefair.lombok") version "8.6"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.a8043"
version = "1.0-SNAPSHOT"

javafx {
    version = "21.0.4"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("org.a8043.gui.Main")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.file("test/${project.name}")
    doFirst {
        if (!workingDir.exists()) workingDir.mkdirs()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":data-object"))

    implementation("org.slf4j:slf4j-api:2.0.18")
    implementation("org.apache.logging.log4j:log4j-core:2.26.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.26.0")

    implementation("cn.hutool:hutool-core:5.8.38")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.46")
    implementation("com.dtflys.forest:forest-core:1.7.6")
    implementation("io.github.typhon0:AnimateFX:1.3.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    workingDir = rootProject.file("test/$name")
}
