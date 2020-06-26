// RotationShiftBitInstructionsTest.kt
// Version 1.0

package monkeygb.cpu

import org.junit.Test
import kotlin.test.assertEquals

class RotationShiftBitInstructionsTest {
    @Test
    fun rotationShiftBitTest() {
        val cpu = Cpu()
        val test = RotationShiftBitInstructions(cpu)

        // rrc
        cpu.registers.c = 0x1
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x0)
        cpu.registers.carryFlag = false
        test.rrc(0x09)
        assertEquals(cpu.registers.c, 0x80)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, false)
        test.rrc(0x0e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x0)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, true)

        // rr
        cpu.registers.a = 0x1
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x8a)
        cpu.registers.carryFlag = false
        test.rr(0x1f)
        assertEquals(cpu.registers.a, 0x0)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.carryFlag = false
        test.rr(0x1e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x45)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, false)

        // rlc
        cpu.registers.b = 0x85
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x0)
        cpu.registers.carryFlag = false
        test.rlc(0x00)
        assertEquals(cpu.registers.b, 0x0b)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, false)
        cpu.registers.carryFlag = false
        test.rr(0x06)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x0)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, true)

        // rl
        cpu.registers.l = 0x80
        cpu.registers.carryFlag = false
        test.rl(0x15)
        assertEquals(cpu.registers.l, 0x00)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.carryFlag = false
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x11)
        test.rl(0x16)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x22)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, false)

        // sla
        cpu.registers.d = 0x80
        cpu.registers.carryFlag = false
        test.sla(0x22)
        assertEquals(cpu.registers.d, 0x0)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.carryFlag = false
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0xff)
        test.sla(0x16)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0xfe)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, false)

        // sra
        cpu.registers.a = 0x8a
        cpu.registers.carryFlag = false
        test.sra(0x2f)
        assertEquals(cpu.registers.a, 0xc5)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, false)
        cpu.registers.carryFlag = false
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x01)
        test.sra(0x2e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x0)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, true)

        // srl
        cpu.registers.a = 0x01
        cpu.registers.carryFlag = false
        test.srl(0x3f)
        assertEquals(cpu.registers.a, 0x00)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.carryFlag = false
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0xff)
        test.srl(0x3e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x7f)
        assertEquals(cpu.registers.carryFlag, true)
        assertEquals(cpu.registers.zeroFlag, false)

        // swap
        cpu.registers.a = 0x00
        test.swap(0x37)
        assertEquals(cpu.registers.a, 0x00)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0xf0)
        test.swap(0x3e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x0f)
        assertEquals(cpu.registers.carryFlag, false)
        assertEquals(cpu.registers.zeroFlag, false)

        // bit
        cpu.registers.a = 0x80
        test.bit(0x7f)
        assertEquals(cpu.registers.a, 0x80)
        assertEquals(cpu.registers.zeroFlag, false)
        cpu.registers.l = 0xef
        test.bit(0x65)
        assertEquals(cpu.registers.l, 0xef)
        assertEquals(cpu.registers.zeroFlag, true)
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0xfe)
        test.bit(0x4e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0xfe)
        assertEquals(cpu.registers.zeroFlag, false)
        test.bit(0x46)
        assertEquals(cpu.registers.zeroFlag, true)

        // set
        cpu.registers.c = 0x80
        cpu.registers.h = 0x3b
        test.set(0xd9)
        test.set(0xfc)
        assertEquals(cpu.registers.c, 0x88)
        assertEquals(cpu.registers.h, 0xbb)
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0x00)
        test.set(0xde)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0x08)

        // res
        cpu.registers.c = 0x80
        cpu.registers.h = 0x3b
        test.res(0xb9)
        test.res(0x8c)
        assertEquals(cpu.registers.c, 0x0)
        assertEquals(cpu.registers.h, 0x39)
        cpu.registers.setHL(0xc100)
        cpu.memoryMap.setValue(cpu.registers.getHL(), 0xff)
        test.res(0x9e)
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0xf7)


    }
}