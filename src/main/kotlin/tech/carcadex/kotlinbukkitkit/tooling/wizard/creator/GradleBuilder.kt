package tech.carcadex.kotlinbukkitkit.tooling.wizard.creator

import tech.carcadex.kotlinbukkitkit.tooling.wizard.*
import tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit.ServerAPI
import tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit.dependencies
import tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit.repositories
import java.io.File

fun generateGradleBuildFile(
        root: File
): File = File(root, "build.gradle.kts")

fun generateGradleSettingsFile(
        root: File
): File = File(root, "settings.gradle.kts")

fun generateConfigFile(
    root: File
): File = File(root, "src/main/resources/config.yml")

fun generateMessagesFile(
    root: File
): File = File(root, "src/main/resources/messages.yml")

fun generateGradleBuildFileContent(
        config: KBAPIModuleConfig
): String = with(config) {
    val kbapiDependencies = kbAPIVersion.dependencies
            .dependenciesAsGradle(if (config.serverVersion.minor >= 16 && !config.kbAPIVersion.version.endsWith("SNAPSHOT")) "library" else "implementation")
    val kbapiDependenciesCompileOnly = kbAPIVersion.dependenciesCompileOnly
        .dependenciesAsGradle("compileOnly")
    val kbapiDependenciesImplementation = kbAPIVersion.dependenciesCompileOnly
        .dependenciesAsGradle("implementation")
    val kspModules = kbAPIVersion.kspModules.dependenciesAsGradle("ksp")
    val kspDependencies = kbAPIVersion.kspModules.dependenciesAsGradle()

    val kbapiRepositories = kbAPIVersion.repositories
            .repositoriesAsGradle()

    val serverApi = ServerAPI(config.serverVersion, config.serverType)
    val serverRepositories = serverApi.repositories.repositoriesAsGradle()
    val serverDependencies = serverApi.dependencies.dependenciesAsGradle()

    val pluginDependencies = "${DEFAULT_INDENTATION}val transitive = Action<ExternalModuleDependency> { isTransitive = false }\n" + externalPlugins
            .flatMap { it.versions.first { it.serverVersion == serverVersion }.dependencies }
            .distinct()
            .dependenciesAsGradle(with = ", transitive")

    val pluginRepositories = externalPlugins
            .flatMap { it.repositories }
            .distinct()
            .repositoriesAsGradle()


    val plugins = (externalPlugins.map{ "\"${it.name}\"" }).joinToString()

    return """
        |// More about the setup here: https://red-tea.gitbook.io/kotlinbukkitkit
        |
        |plugins {
        |    kotlin("jvm") version "${kbAPIVersion.kotlinVersion}"
        |    kotlin("plugin.serialization") version "${kbAPIVersion.kotlinVersion}"
        |    id("net.minecrell.plugin-yml.bukkit") version "$PLUGIN_YML"
        |    id("com.github.johnrengelman.shadow") version "$SHADOW_PLUGIN"
        |    id("xyz.jpenilla.run-paper") version "$RUN_PAPER"
        |    id("com.google.devtools.ksp") version "$KSP"
        |}
        |
        |group = "$artifactGroup"
        |version = "$artifactVersion"
        |val kbk_version = "${config.kbAPIVersion.version}"
        |
        |repositories {
        |    mavenCentral()
        |    // minecraft
        |$serverRepositories
        |
        |    //kotlinbukkitapi with backup repo
        |$kbapiRepositories
        |    
        |    //plugins
        |$pluginRepositories
        |}
        |
        |dependencies {
        |    compileOnly(kotlin("stdlib-jdk8"))
        |
        |    //minecraft
        |$serverDependencies
        |
        |    //kotlinbukkitapi
        |$kbapiDependencies
        |$kbapiDependenciesCompileOnly
        |$kbapiDependenciesImplementation
        |
        |   //ksp
        |$kspDependencies
        |$kspModules
        |
        |    //plugins
        |$pluginDependencies
        |}
        |
        |bukkit {
        |    main = "${packageName}.${pluginMainName}Plugin"
        |    depend = listOf($plugins)
        |    description = "$description"
        |    author = "$author"
        |    website = "$website"
        |    apiVersion = "${config.serverVersion.displayName}"
        |}
        |
        |tasks {
        |//    shadowJar {
        |//        minimize {
        |//            exclude(dependency("org.jetbrains:.*:.*"))
        |//        }
        |//    }
        |    runServer {
        |       minecraftVersion("${config.serverVersion.maxVersion}")
        |   }
        |}
        |
        |
        |sourceSets {
        |   main {
        |       kotlin.srcDirs.add(File("build/generated/ksp"))
        |   }
        |}
        |
    """.trimMargin()
}

private inline fun List<String>.dependenciesAsGradle(configuration: String = "compileOnly", with: String = ""): String =
        joinToString("\n") {
            "$configuration(\"${it}\"$with)"
        }.prependIndent(DEFAULT_INDENTATION)


private inline fun List<String>.repositoriesAsGradle(): String =
        joinToString("\n") {
            """
                |maven {
                |   url = uri("$it")
                |   isAllowInsecureProtocol = true
                |}
            """.trimMargin()
        }.prependIndent(DEFAULT_INDENTATION)

fun generateGradleSettingsFileContent(
        config: KBAPIModuleConfig
): String = with(config) {
    return """
        rootProject.name = "$artifactId"
    """.trimSource()
}

fun String.trimSource(): String {
    val lines = lines()
    val indentation = lines[1].indexOfFirst { !it.isWhitespace() }

    return lines.joinToString("\n") { it.removePrefix(" ".repeat(indentation)) }
}