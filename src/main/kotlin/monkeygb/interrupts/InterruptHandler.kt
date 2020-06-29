// InterruptHandler.kt
// Version 1.0
// Implements interrupt requesting and handling

package monkeygb.interrupts

import monkeygb.cpu.Cpu
import monkeygb.memory.INTERRUPT_ENABLE
import monkeygb.memory.INTERRUPT_FLAG

class InterruptHandler(private val cpu: Cpu) {

    // request a specific interrupt
    fun requestInterrupt(interruptType: InterruptsEnum) {
        val oldFlags = cpu.memoryMap.getValue(INTERRUPT_FLAG)
        when (interruptType) {
            InterruptsEnum.V_BLANK_INTERRUPT -> cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags or 0b00000001)
            InterruptsEnum.LCD_INTERRUPT -> cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags or 0b00000010)
            InterruptsEnum.TIMER_INTERRUPT -> cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags or 0b00000100)
            InterruptsEnum.SERIAL_INTERRUPT -> cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags or 0b00001000)
            InterruptsEnum.JOYPAD_INTERRUPT -> cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags or 0b00010000)
        }
    }

    // check if we have to handle an interrupt
    fun checkInterrupts() {
        if (cpu.ime) {
            val flags = cpu.memoryMap.getValue(INTERRUPT_FLAG)

            // if there are any interrupts to handle
            if(flags and 0x1f > 0)
                // checks all interrupts request from most priority interrupt
                for (i in 0..4)
                    if ((cpu.memoryMap.getValue(INTERRUPT_ENABLE) shr i) and 0x1 == 0x1)    // if interrupt is enabled
                        if ((cpu.memoryMap.getValue(INTERRUPT_FLAG) shr i) and 0x1 == 0x1) {   //if we have an interrupt request
                            var interruptType: InterruptsEnum = InterruptsEnum.V_BLANK_INTERRUPT
                            when (i) {
                                0 -> interruptType = InterruptsEnum.V_BLANK_INTERRUPT
                                1 -> interruptType = InterruptsEnum.LCD_INTERRUPT
                                2 -> interruptType = InterruptsEnum.TIMER_INTERRUPT
                                3 -> interruptType = InterruptsEnum.SERIAL_INTERRUPT
                                4 -> interruptType = InterruptsEnum.JOYPAD_INTERRUPT
                            }
                            handleInterrupt(interruptType)
                            break
                        }
        }
    }

    //handle the specific interrupt
    private fun handleInterrupt(interruptType: InterruptsEnum) {
        cpu.ime = false     // disable interrupt master flag

        // push program counter onto the stack
        cpu.memoryMap.setValue(cpu.registers.stackPointer - 1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer - 2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2

        // remove 1 to interrupt flag register
        val oldFlags = cpu.memoryMap.getValue(INTERRUPT_FLAG)
        when (interruptType) {
            InterruptsEnum.V_BLANK_INTERRUPT -> {
                cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags and 0b11111110)
                cpu.registers.programCounter = 0x40
            }
            InterruptsEnum.LCD_INTERRUPT -> {
                cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags and 0b11111101)
                cpu.registers.programCounter = 0x48
            }
            InterruptsEnum.TIMER_INTERRUPT -> {
                cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags and 0b11111011)
                cpu.registers.programCounter = 0x50
            }
            InterruptsEnum.SERIAL_INTERRUPT -> {
                cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags and 0b11110111)
                cpu.registers.programCounter = 0x58
            }
            InterruptsEnum.JOYPAD_INTERRUPT -> {
                cpu.memoryMap.setValue(INTERRUPT_FLAG, oldFlags and 0b11101111)
                cpu.registers.programCounter = 0x60
            }
        }

        // ISR takes 5 machine cycles
        cpu.machineCycles += 5
    }
}