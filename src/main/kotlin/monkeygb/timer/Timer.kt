// Timer.kt
// Version 1.0
// Implements Game Boy timer

package monkeygb.timer

import monkeygb.getBit
import monkeygb.interrupts.InterruptHandler
import monkeygb.interrupts.InterruptsEnum
import monkeygb.memory.MemoryMap
import monkeygb.memory.TAC
import monkeygb.memory.TIMA
import monkeygb.memory.TMA

class Timer(private val memoryMap: MemoryMap, private val interruptHandler: InterruptHandler) {
    var timerCounter = 1024
    var dividerCounter = 0
    var currentFrequency = memoryMap.getValue(TAC) and 0x3

    fun updateTimers(cycles: Int) {
        updateDividerRegister(cycles)

        val newFrequency = (memoryMap.getValue(TAC) and 0x3)
        if (currentFrequency != newFrequency) {
            currentFrequency = newFrequency
            setClockFrequency()
        }

        // clock must be enabled
        if (isClockEnabled()) {
            timerCounter -= cycles
            if (timerCounter <= 0) {
                setClockFrequency()

                // if timer is about to overflow
                if (memoryMap.getValue(TIMA) == 255) {
                    memoryMap.setValue(TIMA, memoryMap.getValue(TMA))
                    interruptHandler.requestInterrupt(InterruptsEnum.TIMER_INTERRUPT)
                }
                else        // increment TIMA
                    memoryMap.setValue(TIMA, memoryMap.getValue(TIMA) +1)
            }
        }
    }

    private fun isClockEnabled() = getBit(memoryMap.getValue(TAC), 2)

    private fun updateDividerRegister(cycles: Int) {
        dividerCounter += cycles
        if (dividerCounter >= 255) {
            dividerCounter = 0
            memoryMap.incrementDiv()
        }
    }

    // reset timer to the value specified by TAC
    private fun setClockFrequency() {
        timerCounter = when (memoryMap.getValue(TAC) and 0x3) {
            0 -> 1024
            1 -> 16
            2 -> 64
            3 -> 256
            else -> timerCounter
        }
    }
}