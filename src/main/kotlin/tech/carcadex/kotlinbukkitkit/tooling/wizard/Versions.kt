package tech.carcadex.kotlinbukkitkit.tooling.wizard

object Versions {
    val `1-0-0-3-SNAPSHOT` = KotlinBukkitAPIVersion("1.0.0.3-SNAPSHOT")

    val ALL = arrayOf(
        `1-0-0-3-SNAPSHOT`
    )

    val LAST = `1-0-0-3-SNAPSHOT`

    val visitorAll = ALL.associate { it.version to it }

    fun fromString(version: String) = visitorAll[version]
}

data class KotlinBukkitAPIVersion(
    val version: String,
    val kotlinVersion: String = "1.9.23"
) {
    override fun toString(): String {
        return version
    }

    private val group = "tech.carcadex"

    val repositories = listOf(
        "https://hub.spigotmc.org/nexus/content/repositories/snapshots/",
        "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    )

    val dependencies = listOf(
        "${group}:kotlinbukkitkit-architecture:\$kbk_version",
        "${group}:kotlinbukkitkit-extensions:\$kbk_version",
        "${group}:kotlinbukkitkit-serialization:\$kbk_version",
        "${group}:kotlinbukkitkit-messages:\$kbk_version",
        "${group}:kotlinbukkitkit-commands:\$kbk_version",
        "${group}:kotlinbukkitkit-utility:\$kbk_version",
        "${group}:kotlinbukkitkit-menu:\$kbk_version",

        "org.jetbrains.kotlin:kotlin-stdlib",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1",
        "com.github.cryptomorin:XSeries:9.2.0"
    )

    val dependenciesCompileOnly = listOf(
        "org.jetbrains:annotations:24.0.0",
    )

    val kspModules = listOf(
        "${group}:kotlinbukkitkit-genref:$version"
    )
}