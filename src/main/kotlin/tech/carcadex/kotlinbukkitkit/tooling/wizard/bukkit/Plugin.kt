package tech.carcadex.kotlinbukkitkit.tooling.wizard.bukkit

data class Plugin(
        val name: String,
        val versions: List<PluginVersion>,
        val repositories: Set<String>,
        val displayName: String = name
) {
    var isSelected = false
}