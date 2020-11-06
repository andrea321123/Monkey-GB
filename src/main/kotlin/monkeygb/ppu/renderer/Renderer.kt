// Renderer.kt
// Version 1.2
// Render the display from a framebuffer

package monkeygb.ppu.renderer

import monkeygb.joypad.Joypad
import monkeygb.ppu.RENDER_HEIGHT
import monkeygb.ppu.RENDER_WIDTH
import java.awt.Color

import java.awt.Graphics
import java.awt.GridLayout
import java.awt.image.BufferedImage
import javax.swing.*

class Renderer (private val joypad: Joypad): JPanel() {
    private val image = BufferedImage(RENDER_WIDTH *2, RENDER_HEIGHT *2, BufferedImage.TYPE_INT_RGB)
    private val imageLabel = JLabel(ImageIcon(image))       // image is encapsulated in a JLabel
    private val frame = JFrame()

    init {
        // init panel
        layout = GridLayout(1,1)
        add(imageLabel)

        // init frame
        frame.layout = GridLayout(1,1)
        frame.addKeyListener(joypad)
        frame.title = "Monkey-GB"
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
        frame.isResizable = false
        frame.add(this)
        frame.pack()
    }

    override fun paintComponent(g: Graphics) {
        imageLabel.paint(g)
    }

    fun renderDisplay(frameBuffer: Array<Array<Color>>) {
        for (i in 0 until RENDER_HEIGHT)
            for (j in 0 until RENDER_WIDTH) {
                image.setRGB(j *2, i *2, frameBuffer[i][j].rgb)
                image.setRGB(j *2, i *2 +1, frameBuffer[i][j].rgb)
                image.setRGB(j *2 +1, i *2, frameBuffer[i][j].rgb)
                image.setRGB(j *2 +1, i *2 +1, frameBuffer[i][j].rgb)
            }

        paint(graphics)
    }
}