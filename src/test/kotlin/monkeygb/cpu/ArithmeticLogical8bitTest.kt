// ArithmeticLogical8bitTest.kt
// Version 1.1

package monkeygb.cpu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArithmeticLogical8bitTest {
    @Test
    fun arithmeticTest() {
        val cpu = Cpu()
        val test = ArithmeticLogical8bit(cpu)

        cpu.registers.a = 0x02
        cpu.registers.b = 0x40
        cpu.registers.c = 0xc6
        cpu.registers.d = 0xc6
        cpu.registers.e = 0xc6
        cpu.registers.h = 0xc6
        cpu.registers.l = 0xc6

        test.op0x80.invoke()
        assertEquals(cpu.registers.a, 0x42)
        assertFalse(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x3a
        test.op0x81.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x3a
        test.op0x82.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x3a
        test.op0x83.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x3a
        test.op0x84.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x3a
        test.op0x85.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)
        cpu.registers.a = 0x0f
        test.op0x87.invoke()
        assertEquals(cpu.registers.a, 0x1e)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.addSubFlag)

        cpu.registers.programCounter = 0xc100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xff)
        cpu.registers.a = 0x3c
        test.op0xc6.invoke()
        assertEquals(cpu.registers.a, 0x3b)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)

        cpu.registers.setHL(0xaad0)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x12)
        cpu.registers.a = 0x3c
        test.op0x86.invoke()
        assertEquals(cpu.registers.a, 0x4e)
        assertFalse(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)

        cpu.registers.b = 0x0f
        cpu.registers.c = 0x0f
        cpu.registers.d = 0x0f
        cpu.registers.e = 0x0f
        cpu.registers.h = 0x0f
        cpu.registers.l = 0x0f

        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x88.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x89.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x8a.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x8b.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x8c.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x8d.invoke()
        assertEquals(cpu.registers.a, 0xf1)
        assertFalse(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0xe1
        cpu.registers.carryFlag = true
        test.op0x8f.invoke()
        assertEquals(cpu.registers.a, 0xc3)
        assertFalse(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)
        cpu.registers.programCounter = 0xc100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xff)
        cpu.registers.carryFlag = false
        cpu.registers.a = 1
        test.op0xce.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertTrue(cpu.registers.carryFlag)
        assertTrue(cpu.registers.halfCarryFlag)
        cpu.registers.setHL(0xd000)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x1)
        test.op0x8e.invoke()
        assertEquals(cpu.registers.a, 0x2)
        assertFalse(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)

        cpu.registers.b = 0x3e
        cpu.registers.c = 0x3e
        cpu.registers.d = 0x3e
        cpu.registers.e = 0x3e
        cpu.registers.h = 0x3e
        cpu.registers.l = 0x3e
        cpu.registers.a = 0x3e
        test.op0x90.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x91.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x92.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x93.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x94.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x95.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        test.op0x97.invoke()
        assertEquals(cpu.registers.a, 0)
        assertTrue(cpu.registers.zeroFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.carryFlag)
        cpu.registers.a = 0x3e
        cpu.registers.programCounter = 0xc100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x0f)
        test.op0xd6.invoke()
        assertEquals(cpu.registers.a, 0x2f)
        assertTrue(cpu.registers.halfCarryFlag)
        assertFalse(cpu.registers.carryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.setHL(0xd100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x40)
        cpu.registers.a = 0x3e
        test.op0x96.invoke()
        assertEquals(cpu.registers.a, 0xfe)
        assertTrue(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)

        cpu.registers.b = 0x2a
        cpu.registers.c = 0x2a
        cpu.registers.d = 0x2a
        cpu.registers.e = 0x2a
        cpu.registers.h = 0x2a
        cpu.registers.l = 0x2a
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x98.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x99.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x9a.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x9b.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x9c.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        test.op0x9d.invoke()
        assertEquals(cpu.registers.a, 0x10)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertFalse(cpu.registers.zeroFlag)
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = false
        test.op0x9f.invoke()
        assertEquals(cpu.registers.a, 0x0)
        assertFalse(cpu.registers.carryFlag)
        assertFalse(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.addSubFlag)
        assertTrue(cpu.registers.zeroFlag)
        cpu.registers.programCounter = 0xc100
        cpu.registers.a = 0x3b
        cpu.registers.carryFlag = true
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x3a)
        test.op0xde.invoke()
        assertEquals(cpu.registers.a, 0)
        assertFalse(cpu.registers.halfCarryFlag)
        cpu.registers.a = 0x3b
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x4f)
        cpu.registers.carryFlag = true
        test.op0x9e.invoke()
        assertEquals(cpu.registers.a, 0xeb)
        assertTrue(cpu.registers.halfCarryFlag)
        assertTrue(cpu.registers.carryFlag)

        cpu.registers.a = 0xff
        cpu.registers.b = 0x12
        test.op0xa0.invoke()
        assertEquals(cpu.registers.a, 0x12)
        cpu.registers.a = 0xff
        test.op0xb0.invoke()
        assertEquals(cpu.registers.a, 0xff)
        cpu.registers.a = 0xff
        cpu.registers.b = 0x0f
        test.op0xa8.invoke()
        assertEquals(cpu.registers.a, 0xf0)

        cpu.registers.a = 0x3c
        cpu.registers.b = 0x2f
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x40)
        test.op0xb8.invoke()
        assertEquals(cpu.registers.zeroFlag, false)
        assertEquals(cpu.registers.halfCarryFlag, true)
        assertEquals(cpu.registers.addSubFlag, true)
        assertEquals(cpu.registers.carryFlag, false)
        cpu.registers.programCounter = 0xd100
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x3c)
        test.op0xfe.invoke()
        assertEquals(cpu.registers.zeroFlag, true)
        assertEquals(cpu.registers.halfCarryFlag, false)
        assertEquals(cpu.registers.addSubFlag, true)
        assertEquals(cpu.registers.carryFlag, false)
        test.op0xbe.invoke()
        assertEquals(cpu.registers.zeroFlag, false)
        assertEquals(cpu.registers.halfCarryFlag, false)
        assertEquals(cpu.registers.addSubFlag, true)
        assertEquals(cpu.registers.carryFlag, true)

        cpu.registers.a = 0xff
        test.op0x3c.invoke()
        assertEquals(cpu.registers.zeroFlag, true)
        assertEquals(cpu.registers.halfCarryFlag, true)
        assertEquals(cpu.registers.addSubFlag, false)
        assertEquals(cpu.registers.a, 0)

        cpu.registers.a = 0x00
        test.op0x3d.invoke()
        assertEquals(cpu.registers.zeroFlag, false)
        assertEquals(cpu.registers.halfCarryFlag, true)
        assertEquals(cpu.registers.addSubFlag, true)
        assertEquals(cpu.registers.a, 0xff)
    }
}