import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.17.2"
    java
    kotlin("jvm") version "1.9.0"
}

val kotlinVersion = "1.9.0"
val kotlinBukkitAPIVersion = "1.0.0.2"

group = "tech.carcadex.kotlinbukkitkit"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
//    version = "2023.3.3"
    version.set("2023.3.3")
    plugins.set(listOf("java", "Kotlin"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}




tasks {
    compileJava   {
        sourceCompatibility =  "17"
        targetCompatibility = "17"
    }
    jar {
        duplicatesStrategy = (DuplicatesStrategy.EXCLUDE)
    }
}

tasks {
    runIde {
        jbrVersion.set("jbr_jcef-17.0.9-windows-x64-b1087.11")
    }
}

tasks {
    publishPlugin {
        token.set(System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken"))
        channels.set(listOf("stable"))
    }
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {


    pluginDescription.set("""
        <img src="https://github.com/DevSrSouza/KotlinBukkitAPI/raw/master/logo.png" width="417" height="161"/>

        <br />

        The KotlinBukkitKit Tooling is plugin for IntelliJ that helps
        <br />
        developers using <a href="https://github.com/CarcadeX/KotlinBukkitKit-Tooling</a>.
        <br />
        This libraries help build extensions for Minecraft Server using Spigot server.

        <br />

        <br />

        <ul>

        <li<a href='https://github.com/CarcadeX/KotlinBukkitKit'>KotlinBukkitKit</a></li>
        <li<a href='https://github.com/DevSrSouza/KotlinBukkitAPI-Tooling'>KotlinBukkitAPI-Tooling (this plugin)</a></li>
        <li<a href='https://github.com/DevSrSouza/KotlinBukkitAPI'>KotlinBukkitAPI</a></li>
        <li><a href='https://github.com/KotlinMinecraft/KotlinBukkitAPI-Examples'>KotlinBukkitAPI Examples</a></li>
        </ul>

        <br />
        <br />

        <h3>Demonstration</h3>

        <br />

        <img src="https://i.imgur.com/exlwVUs.gif" width='680' height='390'/>
    """.trimIndent())


    changeNotes.set("""
        <h3>0.0.1</h3>

        <ul>
        <li>KotlinBukkitAPI Project Wizard</li>
        <li>Menu Preview tab only avaiable on projects that was KotlinBukkitAPI.</li>
        </ul>
    """.trimIndent())
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(120, "seconds")
}



