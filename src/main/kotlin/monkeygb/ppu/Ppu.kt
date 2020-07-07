// Ppu.kt
// Version 1.1
// Implements the  Game Boy Pixel Processing Unit

package monkeygb.ppu

import monkeygb.getBit
import monkeygb.memory.LCDC
import monkeygb.memory.MemoryMap

import java.awt.Color

const val RENDER_HEIGHT = 144
const val RENDER_WIDTH = 160

class Ppu(private val memoryMap: MemoryMap) {
    // array of colors that represent pixel to render
    var renderWindow = arrayOf<Array<Color>>()

    init {      // initialize renderWindow
        for (i in 0 until RENDER_HEIGHT) {
            var singleLine = arrayOf<Color>()
            for (j in 0 until RENDER_WIDTH)
                singleLine += Color.WHITE
            renderWindow += singleLine
        }
    }

    // we draw a single line on the screen
    fun drawScanLine() {
        val lcdc = memoryMap.getValue(LCDC)
        if (getBit(lcdc, 0))    // BG enable
            drawTiles()
        if (getBit(lcdc, 3))    // sprite enable
            drawSprites()
    }

    private fun drawTiles() {

    }
    private fun drawSprites() {

    }
}