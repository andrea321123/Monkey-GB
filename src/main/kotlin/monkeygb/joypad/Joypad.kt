// Joypad.kt
// Version 1.0
// Implements Game Boy joypad

package monkeygb.joypad

import monkeygb.interrupts.InterruptHandler
import monkeygb.interrupts.InterruptsEnum
import monkeygb.memory.MemoryMap
import monkeygb.setBit
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class Joypad(private val memoryMap: MemoryMap, private val interruptHandler: InterruptHandler): KeyListener {
    private val up = 38
    private val right = 39
    private val down = 40
    private val left = 37
    private val aButton = 90
    private val bButton = 88
    private val start = 10
    private val select = 16



    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {
        when (e?.keyCode) {
            down -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 3, false)
            up -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 2, false)
            left -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 1, false)
            right -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 0, false)

            start -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 3, false)
            select -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 2, false)
            bButton -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 1, false)
            aButton -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 0, false)
        }
        interruptHandler.requestInterrupt(InterruptsEnum.JOYPAD_INTERRUPT)
    }

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyCode) {
            down -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 3, true)
            up -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 2, true)
            left -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 1, true)
            right -> memoryMap.directionalNibble = setBit(memoryMap.directionalNibble, 0, true)

            start -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 3, true)
            select -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 2, true)
            bButton -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 1, true)
            aButton -> memoryMap.buttonNibble = setBit(memoryMap.buttonNibble, 0, true)
        }
    }
}