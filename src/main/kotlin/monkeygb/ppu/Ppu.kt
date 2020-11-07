// Ppu.kt
// Version 1.5
// Implements the  Game Boy Pixel Processing Unit

package monkeygb.ppu

import monkeygb.complement2toInt
import monkeygb.getBit
import monkeygb.memory.*

import java.awt.Color
import java.awt.image.BufferedImage

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
        if (getBit(lcdc, 1))    // sprite enable
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
            (scrollY + memoryMap.getValue(LY)) %256
        else
            memoryMap.getValue(LY) - windowY

        // which of the 8 vertical pixel of the current tile are we drawing?
        val tileRow: Int = ((yPos /8).toByte() * 32)

        // drawing the horizontal 160 pixel of the line
        for (pixel in 0 until 160) {
            var xPos = (pixel + scrollX) %256

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
            var color: Color = getColor(colorNum, BGP, memoryMap)
            val column = memoryMap.getValue(LY)

            // safety check
            if ((column<0)||(column>143)||(pixel<0)||(pixel>159))
            {
                continue
            }

            renderWindow[column][pixel] = color
        }

    }

    // based on code found on http://www.codeslinger.co.uk
    private fun drawSprites() {
        val lcdc = memoryMap.getValue(LCDC)
        var useBiggerSprites: Boolean = getBit(lcdc, 2)

        for (sprite in 0 until 40) {     // for each of the total 40 sprites
            // sprite data occupies 4 bytes in memory
            val index = sprite *4
            val yPos = memoryMap.getValue(0xfe00 + index) -16
            val xPos = memoryMap.getValue(0xfe00 + index +1) -8
            val tileLocation = memoryMap.getValue(0xfe00 + index +2)
            val attributesTable = memoryMap.getValue(0xfe00 + index +3)

            val yFlip = getBit(attributesTable, 6)
            val xFlip = getBit(attributesTable, 5)
            val ly = memoryMap.getValue(LY)
            val spriteSize = if (useBiggerSprites)
                16
            else
                8

            // do we need to draw the sprite on this line?
            if (ly >= yPos && ly < yPos + spriteSize) {
                var line = ly - yPos      // the actual sprite line we're drawing

                // if needed read the sprite in backwards
                if (yFlip) {
                    line -= (spriteSize -1)
                    line *= -1
                }

                line *= 2
                val dataAddress = (0x8000 + (tileLocation *16)) + line
                val data1 = memoryMap.getValue(dataAddress)
                val data2 = memoryMap.getValue(dataAddress +1)

                for (tilePixel in 7 downTo 0) {
                    var colorBit = tilePixel

                    // if needed read the sprite in backwards
                    if (xFlip) {
                        colorBit -= 7
                        colorBit *= -1
                    }

                    // the rest is same as for tiles
                    var colorNum = if (getBit(data2, colorBit))
                        1
                    else
                        0
                    colorNum = colorNum shl 1
                    if (getBit( data1, colorBit))
                        colorNum = colorNum or 1

                    val colorAddress = if (getBit(attributesTable, 4))
                        0xff49
                    else
                        0xff48
                    val color: Color = getColor(colorNum, colorAddress, memoryMap)

                    // bit 0 and 1 of sprite palette mean translucent
                    if (colorNum == 0)
                        continue

                    var xPix = 0- tilePixel
                    xPix += 7
                    val pixel = xPos + xPix

                    // sanity check
                    if ((ly < 0)||(ly > 143)||(pixel < 0)||(pixel > 159)) {
                        continue
                    }

                    // draw color onto the framebuffer
                    renderWindow[ly][pixel] = color
                }
            }
        }
    }
}

fun getColor(colorNum: Int, address: Int, memoryMap: MemoryMap): Color {
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

// draw the tile starting from the pixel [yPos][xPos] on the image
fun drawTile(tileAddress: Int, memoryMap: MemoryMap, image: BufferedImage, xPos: Int, yPos: Int) {
    var currentAddress = tileAddress

    for (row in 0 until 8) {
        val lowByte = memoryMap.getValue(currentAddress)
        val highByte = memoryMap.getValue(currentAddress +1)
        currentAddress +=2

        for (column in 0 until 8) {
            // we get the two bits from the bytes
            val highPixelValue = if (getBit(highByte, (8- column) -1))
                1
            else
                0
            val lowPixelValue = if (getBit(lowByte, (8- column) -1))
                1
            else
                0

            val pixelValue = (highPixelValue shl 1) + lowPixelValue

            // we get the pixel color by applying palette
            val pixelColor: Color = getColor(pixelValue, BGP, memoryMap)
            image.setRGB((xPos + column) *2, (yPos + row) *2, pixelColor.rgb)
            image.setRGB((xPos + column) *2, (yPos + row) *2 +1, pixelColor.rgb)
            image.setRGB((xPos + column) *2 +1, (yPos + row) *2, pixelColor.rgb)
            image.setRGB((xPos + column) *2 +1, (yPos + row) *2 +1, pixelColor.rgb)

        }
    }
}