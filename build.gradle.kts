import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.fabric.loom)
    id("maven-publish")
    id("java")
    id("jacoco")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)

    implementation(libs.fabric.api)

    testImplementation(libs.bundles.testing)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

val minecraftVersion = libs.versions.minecraft.get()

version = System.getenv("VERSION")  ?: "unspecified"
group = project.property("maven_group") as String

val targetJavaVersion = (libs.versions.java.get()).toInt()
val javaVersion = JavaVersion.toVersion(targetJavaVersion)

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    val properties = project.properties.mapKeys { it.key }
        .mapValues { it.value.toString() }
        .toMutableMap()

    properties["minecraft_version"] = minecraftVersion
    properties["loader_version"] = libs.versions.loader.get()

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = targetJavaVersion
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to project.property("mod_name"),
                "Specification-Vendor" to project.property("mod_author"),
                "Specification-Version" to version,
                "Implementation-Title" to project.name,
                "Implementation-Version" to version,
                "Implementation-Vendor" to project.property("mod_author"),
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "Timestamp" to System.currentTimeMillis(),
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                "Build-On-Minecraft" to minecraftVersion
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                packaging = "jar"
                url = "https://github.com/sib-energy-craft/energy-api"

                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://www.mit.edu/~amini/LICENSE.md")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/sib-energy-craft/energy-api.git")
                    developerConnection.set("scm:git:ssh://github.com/sib-energy-craft")
                    url.set("https://github.com/sib-energy-craft/energy-api")
                }

                developers {
                    developer {
                        id.set("sibmaks")
                        name.set("Maksim Drobyshev")
                        email.set("sibmaks@vk.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://nexus.sibmaks.ru/repository/maven-releases/")
            val snapshotsUrl = uri("https://nexus.sibmaks.ru/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.findProperty("nexus_username")?.toString() ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexus_password")?.toString() ?: System.getenv("NEXUS_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sib-energy-craft/energy-api")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}
