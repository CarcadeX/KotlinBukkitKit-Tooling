package tech.carcadex.kotlinbukkitkit.tooling.menu

import com.intellij.util.ImageLoader
import tech.carcadex.kotlinbukkitkit.tooling.Assets
import java.awt.Graphics
import java.awt.Image
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JPanel


class ImagePanel : JPanel() {
    private var image: Image? = null

    init {

        try {
            image = ImageIO.read((File(MenuPreviewFileEditorProvider.ASSETS_FOLDER.absolutePath + "/items-13/acacia_boat.png")))
        } catch (ex: IOException) {
            // handle exception...
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, this) // see javadoc for more info on the parameters
    }
}