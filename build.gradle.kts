plugins {
    `java-library`
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

subprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "com.campmongoose"
    version = "4.3.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    tasks {
        shadowJar {
            dependencies {
                include(dependency("com.github.musician101.musigui:"))
            }

            archiveBaseName.set("ServerSaturday")
            archiveClassifier.set("")
            relocate("io.musician101.musigui", "com.campmongoose.serversaturday.lib.io.musician101.musigui")
            dependsOn("build")
        }
    }
}
