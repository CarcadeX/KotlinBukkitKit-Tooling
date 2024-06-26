package tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit

data class ServerAPI(
        val version: ServerVersion,
        val serverType: ServerType
)

val ServerAPI.dependencies: List<String> get() {

    val versionName = when(version) {
        ServerVersion.v1_8 -> "1.8.8-R0.1-SNAPSHOT"
        ServerVersion.v1_9 -> "1.9.4-R0.1-SNAPSHOT"
        ServerVersion.v1_12 -> "1.12.2-R0.1-SNAPSHOT"
        ServerVersion.v1_13 -> "1.13.2-R0.1-SNAPSHOT"
        ServerVersion.v1_14 -> "1.14.4-R0.1-SNAPSHOT"
        ServerVersion.v1_15 -> "1.15.2-R0.1-SNAPSHOT"
        ServerVersion.v1_16 -> "1.16.5-R0.1-SNAPSHOT"
        ServerVersion.v1_17 -> "1.17.1-R0.1-SNAPSHOT"
        ServerVersion.v1_18 -> "1.18.2-R0.1-SNAPSHOT"
        ServerVersion.v1_19 -> "1.19.4-R0.1-SNAPSHOT"
        ServerVersion.v1_20 -> "1.20.6-R0.1-SNAPSHOT"
        ServerVersion.v1_21 -> "1.21-R0.1-SNAPSHOT"
    }

    val dependency = when(serverType) {
        ServerType.SPIGOT -> "org.spigotmc:spigot-api:$versionName"
        ServerType.PAPERMC -> {
            val v17 = "org.github.paperspigot:paperspigot-api"
            val v19 = "com.destroystokyo.paper:paper-api"
            val v20 = "io.papermc.paper:paper-api"

            when(version) {
                ServerVersion.v1_8 -> "$v17:$versionName"
                ServerVersion.v1_9, ServerVersion.v1_12, ServerVersion.v1_13,
                    ServerVersion.v1_14, ServerVersion.v1_15, ServerVersion.v1_16 -> "$v19:$versionName";
                else -> "$v20:$versionName"
            }
        }
    }

    return listOf(dependency)
}

val ServerAPI.repositories: List<String> get() {
    return when(serverType) {
        ServerType.SPIGOT -> listOf(
                "https://hub.spigotmc.org/nexus/content/repositories/snapshots/",
                "https://oss.sonatype.org/content/repositories/snapshots/"
        )
        ServerType.PAPERMC -> listOf(
                "https://papermc.io/repo/repository/maven-public/",
                "https://oss.sonatype.org/content/repositories/snapshots/"
        )
    }
}