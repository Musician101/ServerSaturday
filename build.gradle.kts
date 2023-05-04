plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow")
}

group = "com.campmongoose"
version = "4.2.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    api("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    //TODO work around since Jitpack can't build the 1.1.0 release for some reason
    api("com.github.musician101.musigui:spigot:e292d9c8e2") {
        exclude("com.google.code.findbugs")
        exclude("org.spigotmc")
    }
    api("com.github.musician101:bukkitier:1.2.2") {
        exclude("org.spigotmc")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    shadowJar {
        dependencies {
            include(dependency("com.github.musician101:"))
            include(dependency("com.github.musician101.musigui:"))
        }

        archiveClassifier.set("")
        relocate("io.musician101", "com.campmongoose.serversaturday.lib.io.musician101")
        dependsOn("build")
    }

    register<Copy>("prepTestJar") {
        dependsOn("shadowJar")
        from("build/libs/${project.name}-${project.version}.jar")
        into("server/plugins")
    }
}
