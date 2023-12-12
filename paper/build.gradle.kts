repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    api(project(":common"))
    //api("io.musician101:bukkitier:1.3.3")  {
    api("com.github.Musician101:Bukkitier:1.3.3")  {
        exclude("org.spigotmc")
    }
    //api("io.musician101.musigui:paper:1.2.2") {
    api("com.github.musician101.musigui:paper:1.2.2") {
        exclude("io.papermc.paper")
    }
    compileOnlyApi("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {

        dependencies {
            include(dependency(":common"))
            include(dependency("com.github.Musician101:Bukkitier:"))
        }

        relocate("io.musician101.bukkitier", "com.campmongoose.serversaturday.lib.io.musician101.bukkitier")
        dependsOn("build")
    }
}
