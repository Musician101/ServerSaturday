import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
}

group = "com.campmongoose"
version = "4.3.0-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    api("com.github.Musician101.MusiGUI:paper:3fb38265d4")
    api("com.github.Musician101.MusiCommand:paper:be49f96ace")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        dependencies {
            include(dependency("com.github.Musician101.MusiCommand:.*"))
            include(dependency("com.github.Musician101.MusiGUI:.*"))
        }

        archiveClassifier = ""
        relocate("io.musician101.musicommand", "com.campmongoose.serversaturday.lib.io.musician101.musicommand")
        relocate("io.musician101.musigui", "com.campmongoose.serversaturday.lib.io.musician101.musigui")
    }

    runServer {
        minecraftVersion("1.21.11")
    }
}

paperPluginYaml {
    main = "com.campmongoose.serversaturday.ServerSaturday"
    author = "Musician101"
    apiVersion = "1.21.11"
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

    foliaSupported = true;
}
