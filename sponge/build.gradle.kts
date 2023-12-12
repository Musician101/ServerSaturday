import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("org.spongepowered.gradle.plugin") version "2.1.1"
}

repositories {
    //TODO temp due to new project
    mavenLocal()
}

dependencies {
    api(project(":common"))
    api("com.github.musician101.musigui:sponge:1.2.2") {
        exclude("org.spongepowered:spongeapi")
    }
    //TODO temp dependency due to new project
    api("io.musician101:spongecmd:1.0-SNAPSHOT")
}

sponge {
    apiVersion("10.0.0")
    plugin("serversaturday") {
        loader {
            name(PluginLoaders.JAVA_PLAIN)
            version("1.0")
        }
        version("${project.version}")
        license("MIT")
        displayName("ServerSaturday")
        entrypoint("com.campmongoose.serversaturday.sponge.SpongeServerSaturday")
        description("Plugin based build submission and feature system.")
        contributor("Musician101") {
            description("Plugin Creator and Lead Dev")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

tasks {
    shadowJar {
        dependencies {
            include(dependency(":common"))
            include(dependency("io.musician101:"))
        }

        relocate("io.musician101.spongecmd", "com.campmongoose.serversaturday.lib.io.musician101.spongecmd")
    }
}
