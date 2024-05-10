import xyz.jpenilla.resourcefactory.bukkit.Permission

buildscript {
    configurations {
        classpath {
            resolutionStrategy {
                force("org.ow2.asm:asm:9.6")
                force("org.ow2.asm:asm-commons:9.6")
            }
        }
    }
}

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.7.0"
    id("xyz.jpenilla.run-paper") version "2.2.4"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
}

group = "com.campmongoose"
version = "4.3.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    api("com.github.Musician101:Bukkitier:2.0.0")
    api("com.github.Musician101.MusiGui:paper:1.2.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
    }

    shadowJar {
        dependencies {
            include(dependency("com.github.Musician101:"))
            include(dependency("com.github.Musician101.MusiGui:"))
        }

        archiveClassifier = ""
        relocate("io.musician101.bukkitier", "com.campmongoose.serversaturday.lib.io.musician101.bukkitier")
        relocate("io.musician101.musigui", "com.campmongoose.serversaturday.lib.io.musician101.musigui")
        dependsOn("build")
    }

    runServer {
        minecraftVersion("1.20.4")
    }
}

bukkitPluginYaml {
    main = "com.campmongoose.serversaturday.ServerSaturday"
    author = "Musician101"
    apiVersion = "1.20"
    commands.create("serversaturday") {
        aliases.addAll("ss")
        description = "Displays help and plugin info."
        usage = "/serversaturday"
    }
    permissions {
        create("ss.*") {
            default = Permission.Default.OP
            description = "Gives access to all features."
            children("ss.admin", "ss.feature", "ss.reload", "ss.submit", "ss.view", "ss.view.goto")
        }
        create("ss.admin") {
            default = Permission.Default.OP
            description = "Gives access to admin features."
        }
        create("ss.feature") {
            default = Permission.Default.OP
            description = "Gives access to feature related features."
        }
        create("ss.submit") {
            default = Permission.Default.TRUE
            description = "Gives access to the submit features."
        }
        create("ss.view") {
            default = Permission.Default.TRUE
            description = "Gives access to basic view features."
        }
        create("ss.view.goto") {
            default = Permission.Default.TRUE
            description = "Gives access to teleporting to build features."
            children("ss.view")
        }
    }

    //foliaSupported = true;
}
