plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.helloapplication")
    mainClass.set("com.example.helloapplication.HelloApplication")
}

javafx {
    version = "21-ea+24"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("org.xerial:sqlite-jdbc:3.39.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    // ✅ Corrige le chemin relatif (ne commence pas par un slash !)
    imageZip.set(layout.buildDirectory.file("distributions/app-${javafx.platform.classifier}.zip"))

    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))

    launcher {
        name = "Hello"
    }

    jpackage {
        // ✅ Ne met pas imageOptions pour macOS → uniquement icon pour Mac
        installerType = "dmg"
        installerName = "HelloApp"
        appVersion = "1.0.0"
        vendor = "Florentin Fallon"
        icon = "src/main/resources/icon.icns"
    }
}
