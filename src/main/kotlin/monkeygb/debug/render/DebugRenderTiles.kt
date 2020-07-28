// DebugRenderTiles.kt
// Version 1.0
// Renders on debug window all background and windows tiles

package monkeygb.debug.render

import monkeygb.memory.MemoryMap
import monkeygb.ppu.RENDER_HEIGHT
import monkeygb.ppu.RENDER_WIDTH
import monkeygb.ppu.drawTile
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

class DebugRenderTiles(private val memoryMap: MemoryMap): JPanel() {
    private val frame = JFrame()
    var image = BufferedImage(16*8*2, 24*8*2, BufferedImage.TYPE_INT_RGB)

    init {
        // init frame
        frame.contentPane.add(this)
        frame.title = "Background and windows tiles"
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(16*8*2, 24*8*2+22)
        frame.isVisible = true

        update()
    }

    fun update() {
        var tileNumber = 0
        for (i in 0x8000 until 0x9800 step 0x10) {  // for each tile
            drawTile(i, memoryMap, image, (tileNumber %16) *8, (tileNumber /16) *8)
            tileNumber++
        }
        paintComponent(this.graphics)
    }

    override fun paintComponent(g: Graphics) {
        g.drawImage(image, 0, 0, this)
    }
}