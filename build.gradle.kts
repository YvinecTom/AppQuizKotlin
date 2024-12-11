import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.23" // Utilisez cette version
    kotlin("plugin.serialization") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Remplacez Material3 par les composants Compose Material
    implementation(compose.material)
    implementation(compose.materialIconsExtended)

    // Ajoutez ces dépendances
    implementation("org.jetbrains.compose.ui:ui-tooling-preview-desktop:1.6.11")
    implementation("org.jetbrains.compose.runtime:runtime-saveable-desktop:1.6.11")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "IhmCorrige"
            packageVersion = "1.0.0"
        }
    }
}
