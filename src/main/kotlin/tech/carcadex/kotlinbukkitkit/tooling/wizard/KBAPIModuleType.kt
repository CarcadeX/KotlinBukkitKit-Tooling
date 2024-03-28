package tech.carcadex.kotlinbukkitkit.tooling.wizard

import tech.carcadex.kotlinbukkitkit.tooling.Assets
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon

class KBAPIModuleType : ModuleType<KBAPIModuleBuilder>("kotlinbukkitkit") {

    companion object {
        val NAME = "KotlinBukkitKit"
        val DESCRIPTION = "KotlinBukkitKit Quick"
        val ICON = tech.carcadex.kotlinbukkitkit.tooling.Assets.KotlinBukkitAPIIcon
        val INSTANCE = KBAPIModuleType()
    }

    override fun createModuleBuilder(): KBAPIModuleBuilder = KBAPIModuleBuilder()
    override fun getName(): String = NAME
    override fun getDescription(): String = DESCRIPTION
    override fun getNodeIcon(isOpened: Boolean): Icon = ICON
    override fun getIcon(): Icon = ICON

}