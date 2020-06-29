// InterruptHandler.kt
// Version 1.0

package monkeygb.interrupts

import monkeygb.cpu.Cpu
import monkeygb.cpu.JumpCallsInstructions
import monkeygb.memory.INTERRUPT_ENABLE
import monkeygb.memory.INTERRUPT_FLAG
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class InterruptHandlerTest {
    @Test
    fun handleInterruptTest() {
        val cpu = Cpu()
        val test = InterruptHandler(cpu)
        val reti = JumpCallsInstructions(cpu)
        cpu.registers.stackPointer = 0xff
        cpu.registers.programCounter = 0xc100
        cpu.ime = true

        test.requestInterrupt(InterruptsEnum.TIMER_INTERRUPT)
        assertEquals(cpu.memoryMap.getValue(INTERRUPT_FLAG), 0b00000100)

        test.checkInterrupts()
        assertEquals(cpu.registers.programCounter, 0xc100)

        cpu.memoryMap.setValue(INTERRUPT_ENABLE, 0b00000100)
        test.checkInterrupts()
        assertEquals(cpu.registers.programCounter, 0x50)

        // in this moment emi should be false
        assertFalse(cpu.ime)
        cpu.memoryMap.setValue(INTERRUPT_ENABLE, 0b11111111)
        test.requestInterrupt(InterruptsEnum.V_BLANK_INTERRUPT)
        test.checkInterrupts()      // shouldn't do anything
        assertEquals(cpu.registers.programCounter, 0x50)

        reti.op0xd9.invoke()        // RETI
        assertEquals(cpu.registers.programCounter, 0xc100)

        assertEquals(cpu.memoryMap.getValue(INTERRUPT_FLAG), 1)     // because V_BLANK hasn't been handled
    }
}