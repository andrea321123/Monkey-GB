// Lcd.kt
// Version 1.4
// Implements the LCD monitor of the Game Boy

package monkeygb.ppu

import monkeygb.getBit
import monkeygb.interrupts.InterruptHandler
import monkeygb.interrupts.InterruptsEnum
import monkeygb.memory.*
import monkeygb.setBit

const val HORIZONTAL_LINES = 154
const val VISIBLE_HORIZONTAL_LINES = 144

class Lcd(private val memoryMap: MemoryMap, private val interruptHandler: InterruptHandler, private val ppu: Ppu) {
    // possible modes
    // 00 (0): during H_BLANK
    // 01 (1): during V_BLANK
    // 10 (2): during searching OAM
    // 11 (3): during transfer data to LCD driver
    private var mode: Int = 2

    // contains the number of remaining machine cycles before we finish drawing a line
    private var scanlineCounter: Long = 456       // TODO: check this number (456) if it is the exact number of cycles needed

    var enabled: Boolean = true

    fun updateGraphics(lastInstructionCycles: Long) {
        setLcdStatus()

        if (enabled)
            scanlineCounter -= lastInstructionCycles
        else
            return

        if (scanlineCounter <= 0) {     // we finished a line
            scanlineCounter = 456
            memoryMap.incrementLY()
            val currentLine = memoryMap.getValue(LY)
            when {
                currentLine == VISIBLE_HORIZONTAL_LINES -> {       // V_BLANK
                    interruptHandler.requestInterrupt(InterruptsEnum.V_BLANK_INTERRUPT)
                    mode = 0
                }
                // draw the current scanline
                currentLine < 144 -> ppu.drawScanLine()
            }
        }
    }

    private fun setLcdStatus() {
        var status = memoryMap.getValue(STAT)

        if (!isLcdEnabled()) {
            // set the mode to 1 during lcd disabled and reset scanline
            scanlineCounter = 456
            memoryMap.setValue(LY, 0)
            status = status and 252
            status = setBit(status, 0, true)

            memoryMap.setValue(STAT, status)
            return
        }

        val currentLine = memoryMap.getValue(LY)
        val currentMode = status and 0x3

        mode = 0
        var reqInt = false

        // in V_BLANK so set mode to 1
        if (currentLine >= 144) {
            mode = 1;
            status = setBit(status, 0, true)
            status = setBit(status, 1, false)
            reqInt = getBit(status, 4)      // check if there is an interrupt request
        }
        else {
            val mode2Bounds = 456 - 80
            val mode3Bounds = mode2Bounds - 172

            when {
                scanlineCounter >= mode2Bounds -> {     // mode 2
                    mode = 2
                    status = setBit(status, 1, true)
                    status = setBit(status, 0, false)
                    reqInt = getBit(status, 5)
                }
                scanlineCounter >= mode3Bounds -> {     // mode 3
                    // there is no interrupt request in mode 3
                    mode = 3
                    status = setBit(status, 1, true)
                    status = setBit(status, 0, true)
                }
                else -> {                               // mode 0
                    mode = 0
                    status = setBit(status, 1, false)
                    status = setBit(status, 0, false)
                    reqInt = getBit(status, 3)
                }
            }

        }

        // just entered a new mode so request interrupt
        if (reqInt && (mode != currentMode))
            interruptHandler.requestInterrupt(InterruptsEnum.LCD_INTERRUPT)

        // check the coincidence flag
        if (currentLine == memoryMap.getValue(LYC)) {
            status = setBit(status, 2, true)
            if (getBit(status, 6))      // coincidence interrupt enabled
                interruptHandler.requestInterrupt(InterruptsEnum.LCD_INTERRUPT)
        }
        else {
            status = setBit(status, 2, false)
        }

        //write status to STAT register in memory
        memoryMap.setValue(STAT, status)

    }

    private fun isLcdEnabled(): Boolean = true
}