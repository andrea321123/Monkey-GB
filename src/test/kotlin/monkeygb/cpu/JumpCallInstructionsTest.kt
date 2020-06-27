// JumpCallInstructionsTest.kt
// Version 1.0

package monkeygb.cpu

import org.junit.Test
import kotlin.test.assertEquals

class JumpCallInstructionsTest {
    @Test
    fun jumpTest() {
        val cpu = Cpu()
        val test = JumpCallsInstructions(cpu)

        cpu.registers.programCounter = 0xc100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x00)
        cpu.memoryMap.setValue(cpu.registers.programCounter +1, 0xd0)

        test.op0xc3.invoke()
        assertEquals(cpu.registers.programCounter, 0xd000)

        cpu.registers.carryFlag = false
        cpu.registers.programCounter = 0xc100
        test.op0xda.invoke()
        assertEquals(cpu.registers.programCounter, 0xc102)
        cpu.registers.programCounter = 0xc100
        test.op0xd2.invoke()
        assertEquals(cpu.registers.programCounter, 0xd000)

        cpu.memoryMap.setValue(cpu.registers.programCounter, 0b11001100)
        test.op0x18.invoke()
        assertEquals(cpu.registers.programCounter, 0xcfcd)
    }
}