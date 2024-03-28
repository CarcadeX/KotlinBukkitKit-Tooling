package tech.carcadex.kotlinbukkitkit.tooling.menu

import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.incremental.createDirectory
import tech.carcadex.kotlinbukkitkit.tooling.Assets
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class MenuPreviewFileEditorProvider : WeighedFileEditorProvider() {

    companion object {
        val MAIN_FOLDER = File("kbk-assets")
        val ASSETS_FOLDER = File("kbk-assets/assets")
        const val ID = "kotlinbukkitkit-menu-preview"
        init {
            MAIN_FOLDER.createDirectory()
            val zip = File(MAIN_FOLDER, "assets.zip")
            if(!zip.exists()) {
                zip.createNewFile()
                val input: InputStream = Assets::class.java.getResourceAsStream("/assets.zip")
                zip.writeBytes(input.readAllBytes())

                fun unzip(`is`: InputStream?, targetDir: Path) {
                    var targetDir: Path = targetDir
                    targetDir = targetDir.toAbsolutePath()
                    ZipInputStream(`is`).use { zipIn ->
                        var ze: ZipEntry
                        while (zipIn.nextEntry.also {
                                if (it != null) {
                                    ze = it
                                    val resolvedPath: Path = targetDir.resolve(ze.name).normalize()
                                    if (!resolvedPath.startsWith(targetDir)) {
                                        throw RuntimeException(
                                            "Entry with an illegal path: "
                                                    + ze.name
                                        )
                                    }
                                    if (ze.isDirectory) {
                                        Files.createDirectories(resolvedPath)
                                    } else {
                                        Files.createDirectories(resolvedPath.getParent())
                                        Files.copy(zipIn, resolvedPath)
                                    }
                                }
                            } != null) {

                        }
                    }
                }

                unzip(FileInputStream(zip), MAIN_FOLDER.toPath())

            }
        }
    }

    override fun getEditorTypeId(): String = ID

    override fun accept(project: Project, file: VirtualFile): Boolean {
        val type = file.fileType

        if(type != KotlinFileType.INSTANCE) return false

        val module = ModuleUtilCore.findModuleForFile(file, project) ?: return false

        val root = ModuleRootManager.getInstance(module)
        var hasKotlinBukkitAPI = false

        root.orderEntries().withoutSdk().forEachLibrary {
            if((it.name ?: "").contains("kotlinbukkitkit", true)) {
                hasKotlinBukkitAPI = true

                false
            } else {
                true
            }
        }

        if(!file.readText().contains("tech.carcadex.kotlinbukkitkit.menu.dsl.menu")) hasKotlinBukkitAPI = false;

        return hasKotlinBukkitAPI
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val editor: TextEditor = TextEditorProvider.getInstance().createEditor(project, file) as TextEditor
        val preview = MenuPreviewFileEditor(project, file, editor)
        val result = TextEditorWithPreview(editor, preview, "KotlinBukkitKit")
        return result
    }

    //override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR


}
