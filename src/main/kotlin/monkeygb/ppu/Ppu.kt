// Ppu.kt
// Version 1.3
// Implements the  Game Boy Pixel Processing Unit

package monkeygb.ppu

import monkeygb.complement2toInt
import monkeygb.getBit
import monkeygb.memory.*

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

    // based on code found on http://www.codeslinger.co.uk
    private fun drawTiles() {
        var tileData: Int = 0
        var backgroundMemory: Int = 0
        var unsigned: Boolean = true       // shows if tile identifier is signed or unsigned
        var usingWindow = false
        var lcdc = memoryMap.getValue(LCDC)

        // where to draw the visual area and the window
        val scrollY = memoryMap.getValue(SCROLL_Y)
        val scrollX = memoryMap.getValue(SCROLL_X)
        val windowY = memoryMap.getValue(WY)
        val windowX = memoryMap.getValue(WX)

        // bit 5 of LCDC shows if window is enabled
        if (getBit(lcdc, 5))
            if (windowY <= memoryMap.getValue(LY))
                usingWindow = true

        // which tile data are we using?
        if (getBit(lcdc, 4))
            tileData = 0x8000
        else {
            tileData = 0x8800
            unsigned = false
        }

        // which background memory?
        backgroundMemory = if (!usingWindow) {
            if (getBit(lcdc, 3))
                0x9c00
            else
                0x9800
        }
        else {      // which window memory?
            if (getBit(lcdc, 6))
                0x9c00
            else
                0x9800
        }

        var yPos: Int = 0       // which of the 32 vertical tiles the scanline is drawing
        yPos = if (!usingWindow)
            scrollY + memoryMap.getValue(LY)
        else
            memoryMap.getValue(LY) - windowY

        // which of the 8 vertical pixel of the current tile are we drawing?
        val tileRow: Int = ((yPos /8).toByte() * 32)

        // drawing the horizontal 160 pixel of the line
        for (pixel in 0 until 160) {
            var xPos = pixel + scrollX

            if (usingWindow)
                if (pixel >= windowX)
                    xPos = pixel - windowX

            // which of the 32 horizontal tiles are we drawing=
            val tileColumn: Int = xPos /8
            var tileNum:Int = 0

            // get the identity number (can be signed or unsigned)
            val tileAddress = backgroundMemory + tileRow + tileColumn
            tileNum = if (unsigned)
                memoryMap.getValue(tileAddress)
            else
                complement2toInt(memoryMap.getValue(tileAddress))

            // obtain where this identifier is in memory
            var tileLocation = tileData
            tileLocation += if (unsigned)
                tileNum * 16
            else
                (tileNum +128) * 16

            // find the correct vertical line we're on of the tile to get the tile data from in memory
            var line: Int = yPos % 8
            line *= 2
            var data1 = memoryMap.getValue(tileLocation + line)
            var data2 = memoryMap.getValue(tileLocation + line +1)

            var colorBit = xPos %8
            colorBit -= 7
            colorBit *= -1

            // combine data 2 and data 1 to get the colour id for this pixel
            var colorNum = if (getBit(data2, colorBit))
                1
            else
                0
            colorNum = colorNum shl 1
            if (getBit( data1, colorBit))
                colorNum = colorNum or 1

            // now we need to convert the color id with the palette at 0xff47
            var color: Color = getColor(colorNum, BGP)
            val column = memoryMap.getValue(LY)

            // safety check
            if ((column<0)||(column>143)||(pixel<0)||(pixel>159))
            {
                println("HO SFONNATO TUTTO FRATELLI")
            }

            renderWindow[column][pixel] = color
        }

    }

    private fun drawSprites() {

    }

    private fun getColor(colorNum: Int, address: Int): Color {
        var palette = memoryMap.getValue(address)
        var high = 0
        var low =  0

        // which bits of the color palette does the color id map to
        when (colorNum) {
            0 -> {
                high = 1
                low = 0
            }
            1 -> {
                high = 3
                low = 2
            }
            2 -> {
                high = 5
                low = 4
            }
            3 -> {
                high = 7
                low = 6
            }
        }

        // use the palette to get the color
        var colorInt = 0    // from colorInt we will get the actual color
        if (getBit(palette, high))
            colorInt = 2
        if (getBit(palette, low))
            colorInt += 1

        // convert colorInt to actual color
        return when (colorInt) {
            0 -> Color.WHITE
            1 -> Color.LIGHT_GRAY
            2 -> Color.DARK_GRAY
            3 -> Color.BLACK
            else -> Color.PINK
        }

    }
}