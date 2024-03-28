package tech.carcadex.kotlinbukkitkit.tooling.wizard.creator

import tech.carcadex.kotlinbukkitkit.tooling.wizard.KBAPIModuleConfig
import java.io.File

fun generatePluginPackageFolder(
        root: File,
        config: KBAPIModuleConfig
): File = File(root, "src/main/kotlin/${config.packageName.replace(".", "/")}")

fun generatePluginMainFile(
        root: File,
        config: KBAPIModuleConfig
): File = File(generatePluginPackageFolder(root, config), "${config.pluginMainName}.kt")

fun generatePluginMainFileContent(
        config: KBAPIModuleConfig
): String = with(config) {
    return """
        package $packageName
        
        import tech.carcadex.kotlinbukkitkit.architecture.KotlinPlugin
        import tech.carcadex.kotlinbukkitkit.genref.plugin.annotations.OnDisable
        import tech.carcadex.kotlinbukkitkit.genref.plugin.annotations.Plugin
        
        @Plugin
        fun KotlinPlugin.start() {
            
        }
        
        @OnDisable
        fun KotlinPlugin.stop() {
            
        }
    """.trimIndent()
}