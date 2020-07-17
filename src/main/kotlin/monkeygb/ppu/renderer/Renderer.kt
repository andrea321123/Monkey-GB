// Renderer.kt
// Version 1.1
// Render the display from a framebuffer

package monkeygb.ppu.renderer

import monkeygb.joypad.Joypad
import monkeygb.memory.MemoryMap
import monkeygb.ppu.Ppu
import monkeygb.ppu.RENDER_HEIGHT
import monkeygb.ppu.RENDER_WIDTH
import java.awt.Color

import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

class Renderer (private val joypad: Joypad): JPanel() {
    var image = BufferedImage(RENDER_WIDTH *2, RENDER_HEIGHT *2, BufferedImage.TYPE_INT_RGB)
    private val frame = JFrame()

    init {
        // init frame
        frame.addKeyListener(joypad)
        frame.contentPane.add(this)
        frame.title = "Monkey-GB"
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(RENDER_WIDTH *2, RENDER_HEIGHT *2+22)
        frame.isVisible = true

        // init panel
        renderDisplay(Ppu(MemoryMap()).renderWindow)
    }

    override fun paintComponent(g: Graphics) {
        g.drawImage(image, 0, 0, this)
    }

    fun renderDisplay(frameBuffer: Array<Array<Color>>) {
        for (i in 0 until RENDER_HEIGHT)
            for (j in 0 until RENDER_WIDTH) {
                image.setRGB(j *2, i *2, frameBuffer[i][j].rgb)
                image.setRGB(j *2, i *2 +1, frameBuffer[i][j].rgb)
                image.setRGB(j *2 +1, i *2, frameBuffer[i][j].rgb)
                image.setRGB(j *2 +1, i *2 +1, frameBuffer[i][j].rgb)
            }

        frame.paint(frame.graphics)
    }
}