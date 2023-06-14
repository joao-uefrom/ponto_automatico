import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "app.jotape.ponto_automatico"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.4.0")
                implementation("org.xerial:sqlite-jdbc:3.42.0.0")
                implementation("org.quartz-scheduler:quartz:2.3.2")
                implementation("org.slf4j:slf4j-nop:2.0.7")
                implementation("org.seleniumhq.selenium:selenium-java:4.9.1")
                implementation("de.taimos:totp:1.0")
                implementation("commons-codec:commons-codec:1.15")
            }

            resources.srcDir("src/jvmMain/resources")
        }
        val jvmTest by getting { }
    }
}

compose.desktop {
    application {
        mainClass = "app.jotape.ponto_automatico.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            modules("java.sql")
            modules("java.rmi")
            packageName = "ponto_automatico"
            packageVersion = "1.0.0"
        }
    }
}
