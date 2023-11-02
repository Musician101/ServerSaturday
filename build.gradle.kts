plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow")
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
    compileOnlyApi("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    api("com.github.Musician101:Bukkitier:1.3.3") {
        exclude("org.spigotmc")
    }
    api("com.github.musician101.musigui:paper:1.2.2") {
        exclude("io.papermc.paper")
    }
    //TODO temp to fix package names
    //api("com.github.Musician101:MusiBoard:1.0.1") {
    api("com.github.Musician101:MusiBoard:master-SNAPSHOT") {
        exclude("io.papermc.paper")
        exclude("com.github.Musician101")
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
            include(dependency("io.musician101.musigui:"))
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
