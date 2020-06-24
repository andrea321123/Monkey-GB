// LoadInstruction16bitTest.kt
// Version 1.0

package monkeygb.cpu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoadInstructions16bitTest {
    @Test
    fun loadTest() {
        val cpu = Cpu()
        val test = LoadInstructions16bit(cpu)

        cpu.registers.programCounter = 0xc100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xaa)
        cpu.memoryMap.setValue(cpu.registers.programCounter +1, 0xbb)
        test.op0x01.invoke()
        assertEquals(cpu.registers.b, 0xbb)
        assertEquals(cpu.registers.c, 0xaa)
        cpu.registers.programCounter -= 2
        test.op0x11.invoke()
        assertEquals(cpu.registers.d, 0xbb)
        assertEquals(cpu.registers.e, 0xaa)
        cpu.registers.programCounter -= 2
        test.op0x21.invoke()
        assertEquals(cpu.registers.h, 0xbb)
        assertEquals(cpu.registers.l, 0xaa)
        cpu.registers.programCounter -= 2
        test.op0x31.invoke()
        assertEquals(cpu.registers.stackPointer, 0xbbaa)

        test.op0xf9.invoke()
        assertEquals(cpu.registers.stackPointer, cpu.registers.getHL())

        cpu.registers.b = 0x05
        cpu.registers.c = 0x0a
        cpu.registers.d = 0x15
        cpu.registers.e = 0x1a
        cpu.registers.h = 0x25
        cpu.registers.l = 0x2a
        cpu.registers.a = 0x35
        cpu.registers.stackPointer = 0xffff
        cpu.registers.carryFlag = true
        cpu.registers.zeroFlag = false
        cpu.registers.addSubFlag = true
        cpu.registers.halfCarryFlag = false

        test.op0xc5.invoke()
        assertEquals(cpu.registers.stackPointer, 0xfffd)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer), cpu.registers.c)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer +1), cpu.registers.b)
        test.op0xd5.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer), cpu.registers.e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer +1), cpu.registers.d)
        test.op0xe5.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer), cpu.registers.l)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer +1), cpu.registers.h)
        test.op0xf5.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.stackPointer +1), cpu.registers.a)

        cpu.registers.b = 0x0
        cpu.registers.c = 0x0
        cpu.registers.d = 0x0
        cpu.registers.e = 0x0
        cpu.registers.h = 0x0
        cpu.registers.l = 0x0
        cpu.registers.a = 0x0
        cpu.registers.carryFlag = false
        cpu.registers.zeroFlag = false
        cpu.registers.addSubFlag = false
        cpu.registers.halfCarryFlag = false
        test.op0xf1.invoke()
        assertTrue(cpu.registers.carryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.zeroFlag)
        assertEquals(cpu.registers.a, 0x35)
        test.op0xe1.invoke()
        assertEquals(cpu.registers.l, 0x2a)
        assertEquals(cpu.registers.h, 0x25)
        test.op0xd1.invoke()
        assertEquals(cpu.registers.e, 0x1a)
        assertEquals(cpu.registers.d, 0x15)
        test.op0xc1.invoke()
        assertEquals(cpu.registers.c, 0xa)
        assertEquals(cpu.registers.b, 0x5)

        cpu.registers.stackPointer = 0xffff
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0b11001100)
        test.op0xf8.invoke()
        assertEquals(cpu.registers.stackPointer, 0xffff -52)

        cpu.registers.stackPointer = 0xffee
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xff)
        cpu.memoryMap.setValue(cpu.registers.programCounter +1, 0xcf)
        test.op0x08.invoke()
        assertEquals(cpu.memoryMap.getValue(0xcfff), 0xee)
        assertEquals(cpu.memoryMap.getValue(0xd000), 0xff)
    }
}
