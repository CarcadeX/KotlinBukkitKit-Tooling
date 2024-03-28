package tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit

class PluginVersion(
        val version: String,
        val serverVersion: ServerVersion,
        val dependencies: Set<String>
)