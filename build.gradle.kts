plugins {
    id("com.gradleup.shadow").version("9.1.0")
    id("java")
}

group = "net.savagedev"
version = "1.1.0"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://jitpack.io")

    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.16.1-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.7")

    implementation("com.github.SavageAvocado:SpigotUpdateChecker:1.0.0")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("mysql:mysql-connector-java:8.0.33")
}

tasks {
    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")

        relocate("com.zaxxer.hikari", "net.savagedev.lunerite.hikaricp")

        minimize()
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(sourceSets.main.get().resources.srcDirs) {
            expand(Pair("version", project.version))
                .include("plugin.yml")
        }
    }
}
