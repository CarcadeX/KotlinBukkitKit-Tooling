package tech.carcadex.kotlinbukkitkit.tooling

import com.intellij.ui.IconManager
import javax.swing.Icon

object Assets {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, ClassLoader.getSystemClassLoader())
    }

    private const val assetFolder = "/assets"

    val KotlinBukkitAPIIcon =
        tech.carcadex.kotlinbukkitkit.tooling.Assets.load("${tech.carcadex.kotlinbukkitkit.tooling.Assets.assetFolder}/icons/kotlinbukkitapi-icon.svg")
    val Bukkript =
        tech.carcadex.kotlinbukkitkit.tooling.Assets.load("${tech.carcadex.kotlinbukkitkit.tooling.Assets.assetFolder}/icons/bukkript.svg")
    val FolderScript =
        tech.carcadex.kotlinbukkitkit.tooling.Assets.load("${tech.carcadex.kotlinbukkitkit.tooling.Assets.assetFolder}/icons/folderScript.svg")
    val FolderPlugins =
        tech.carcadex.kotlinbukkitkit.tooling.Assets.load("${tech.carcadex.kotlinbukkitkit.tooling.Assets.assetFolder}/icons/folderPlugins.svg")
    val FolderBukkript =
        tech.carcadex.kotlinbukkitkit.tooling.Assets.load("${tech.carcadex.kotlinbukkitkit.tooling.Assets.assetFolder}/icons/folderBukkript.svg")
}