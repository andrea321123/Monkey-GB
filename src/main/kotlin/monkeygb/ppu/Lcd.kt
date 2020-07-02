// Lcd.kt
// Version 1.0
// Implements the LCD monitor of the Game Boy

package monkeygb.ppu

import monkeygb.interrupts.InterruptHandler
import monkeygb.interrupts.InterruptsEnum
import monkeygb.memory.LY
import monkeygb.memory.MemoryMap

const val HORIZONTAL_LINES = 154
const val VISIBLE_HORIZONTAL_LINES = 144

class Lcd(private val memoryMap: MemoryMap, private val interruptHandler: InterruptHandler) {
    // possible modes
    // 00 (0): during H_BLANK
    // 01 (1): during V_BLANK
    // 10 (2): during searching OAM
    // 11 (3): during transfer data to LCD driver
    var mode: Int = 2

    // contains the number of remaining machine cycles before we finish drawing a line
    var scanlineCounter = 456       // TODO: check this number (456) if it is the exact number of cycles needed

    var enabled: Boolean = true

    fun updateGraphics(lastInstructionCycles: Int) {
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
                currentLine < 144 -> DrawScanLine()
            };
        }
    }
}