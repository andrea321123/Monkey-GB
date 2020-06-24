// LoadInstruction8bitTest.kt
// Version 1.0

package monkeygb.cpu

import kotlin.test.Test
import kotlin.test.assertEquals

class LoadInstructions8bitTest {
    @Test
    fun loadTest() {
        val cpu = Cpu()
        val test = LoadInstructions8bit(cpu)

        cpu.registers.b = 5
        cpu.registers.c = 10
        cpu.registers.d = 15
        cpu.registers.e = 20
        cpu.registers.h = 25
        cpu.registers.l = 30
        cpu.registers.a = 35

        test.op0x40.invoke()
        assertEquals(cpu.registers.b, 5)
        test.op0x41.invoke()
        assertEquals(cpu.registers.b, 10)
        test.op0x42.invoke()
        assertEquals(cpu.registers.b, 15)
        test.op0x43.invoke()
        assertEquals(cpu.registers.b, 20)
        test.op0x44.invoke()
        assertEquals(cpu.registers.b, 25)
        test.op0x45.invoke()
        assertEquals(cpu.registers.b, 30)
        test.op0x47.invoke()
        assertEquals(cpu.registers.b, 35)

        cpu.registers.b = 5
        test.op0x48.invoke()
        assertEquals(cpu.registers.c, 5)
        test.op0x49.invoke()
        assertEquals(cpu.registers.c, 5)
        test.op0x4a.invoke()
        assertEquals(cpu.registers.c, 15)
        test.op0x4b.invoke()
        assertEquals(cpu.registers.c, 20)
        test.op0x4c.invoke()
        assertEquals(cpu.registers.c, 25)
        test.op0x4d.invoke()
        assertEquals(cpu.registers.c, 30)
        test.op0x4f.invoke()
        assertEquals(cpu.registers.c, 35)

        cpu.registers.c = 10
        test.op0x50.invoke()
        assertEquals(cpu.registers.d, 5)
        test.op0x51.invoke()
        assertEquals(cpu.registers.d, 10)
        test.op0x52.invoke()
        assertEquals(cpu.registers.d, 10)
        test.op0x53.invoke()
        assertEquals(cpu.registers.d, 20)
        test.op0x54.invoke()
        assertEquals(cpu.registers.d, 25)
        test.op0x55.invoke()
        assertEquals(cpu.registers.d, 30)
        test.op0x57.invoke()
        assertEquals(cpu.registers.d, 35)

        cpu.registers.d = 15
        test.op0x58.invoke()
        assertEquals(cpu.registers.e, 5)
        test.op0x59.invoke()
        assertEquals(cpu.registers.e, 10)
        test.op0x5a.invoke()
        assertEquals(cpu.registers.e, 15)
        test.op0x5b.invoke()
        assertEquals(cpu.registers.e, 15)
        test.op0x5c.invoke()
        assertEquals(cpu.registers.e, 25)
        test.op0x5d.invoke()
        assertEquals(cpu.registers.e, 30)
        test.op0x5f.invoke()
        assertEquals(cpu.registers.e, 35)

        cpu.registers.e = 20
        test.op0x60.invoke()
        assertEquals(cpu.registers.h, 5)
        test.op0x61.invoke()
        assertEquals(cpu.registers.h, 10)
        test.op0x62.invoke()
        assertEquals(cpu.registers.h, 15)
        test.op0x63.invoke()
        assertEquals(cpu.registers.h, 20)
        test.op0x64.invoke()
        assertEquals(cpu.registers.h, 20)
        test.op0x65.invoke()
        assertEquals(cpu.registers.h, 30)
        test.op0x67.invoke()
        assertEquals(cpu.registers.h, 35)

        cpu.registers.h = 25
        test.op0x68.invoke()
        assertEquals(cpu.registers.l, 5)
        test.op0x69.invoke()
        assertEquals(cpu.registers.l, 10)
        test.op0x6a.invoke()
        assertEquals(cpu.registers.l, 15)
        test.op0x6b.invoke()
        assertEquals(cpu.registers.l, 20)
        test.op0x6c.invoke()
        assertEquals(cpu.registers.l, 25)
        test.op0x6d.invoke()
        assertEquals(cpu.registers.l, 25)
        test.op0x6f.invoke()
        assertEquals(cpu.registers.l, 35)

        cpu.registers.l = 30
        test.op0x78.invoke()
        assertEquals(cpu.registers.a, 5)
        test.op0x79.invoke()
        assertEquals(cpu.registers.a, 10)
        test.op0x7a.invoke()
        assertEquals(cpu.registers.a, 15)
        test.op0x7b.invoke()
        assertEquals(cpu.registers.a, 20)
        test.op0x7c.invoke()
        assertEquals(cpu.registers.a, 25)
        test.op0x7d.invoke()
        assertEquals(cpu.registers.a, 30)
        test.op0x7f.invoke()
        assertEquals(cpu.registers.a, 30)

        cpu.registers.programCounter = 0xc000
        cpu.memoryMap.setValue(0xc000, 45)
        test.op0x06.invoke()
        assertEquals(cpu.registers.b, 45)
        cpu.registers.programCounter--
        test.op0x0e.invoke()
        assertEquals(cpu.registers.c, 45)
        cpu.registers.programCounter--
        test.op0x16.invoke()
        assertEquals(cpu.registers.d, 45)
        cpu.registers.programCounter--
        test.op0x1e.invoke()
        assertEquals(cpu.registers.e, 45)
        cpu.registers.programCounter--
        test.op0x26.invoke()
        assertEquals(cpu.registers.h, 45)
        cpu.registers.programCounter--
        test.op0x2e.invoke()
        assertEquals(cpu.registers.l, 45)
        cpu.registers.programCounter--
        test.op0x3e.invoke()
        assertEquals(cpu.registers.a, 45)

        cpu.memoryMap.setValue(cpu.registers.programCounter, 127)       //PC: 0xc001
        cpu.registers.setHL(cpu.registers.programCounter)
        test.op0x46.invoke()
        assertEquals(cpu.registers.b, 127)
        test.op0x4e.invoke()
        assertEquals(cpu.registers.c, 127)
        test.op0x56.invoke()
        assertEquals(cpu.registers.d, 127)
        test.op0x5e.invoke()
        assertEquals(cpu.registers.e, 127)
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xd0)
        cpu.memoryMap.setValue(0xd001, 0x55)
        cpu.memoryMap.setValue(0xd055, 0xaa)
        test.op0x66.invoke()
        assertEquals(cpu.registers.h, 0xd0)
        test.op0x6e.invoke()
        assertEquals(cpu.registers.l, 0x55)
        test.op0x7e.invoke()
        assertEquals(cpu.registers.a, 0xaa)

        cpu.registers.b = 5
        cpu.registers.c = 10
        cpu.registers.d = 15
        cpu.registers.e = 20
        cpu.registers.h = 0xc0
        cpu.registers.l = 0x01
        cpu.registers.a = 25
        test.op0x70.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.b)
        test.op0x71.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.c)
        test.op0x72.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.d)
        test.op0x73.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.e)
        test.op0x74.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.h)
        test.op0x75.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.l)
        test.op0x77.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), cpu.registers.a)

        cpu.registers.programCounter = 0xd0aa
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0xff)
        test.op0x36.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getHL()), 0xff)
        cpu.registers.b = 0xc1
        cpu.registers.c = 0xc1
        cpu.registers.d = 0xd2
        cpu.registers.e = 0xd2
        cpu.memoryMap.setValue(cpu.registers.getBC(), 0xab)
        cpu.memoryMap.setValue(cpu.registers.getDE(), 0xcd)
        test.op0x0a.invoke()
        assertEquals(cpu.registers.a, 0xab)
        test.op0x1a.invoke()
        assertEquals(cpu.registers.a, 0xcd)

        cpu.memoryMap.setValue(0xffc1, 0x01)
        test.op0xf2.invoke()
        assertEquals(cpu.registers.a, 0x01)
        cpu.registers.a = 0xfe
        test.op0xe2.invoke()
        assertEquals(cpu.memoryMap.getValue(0xffc1), 0xfe)

        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x66)
        cpu.memoryMap.setValue(0xff66, 0x02)
        test.op0xf0.invoke()
        assertEquals(cpu.registers.a, 0x02)
        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x10)
        test.op0xe0.invoke()
        assertEquals(cpu.memoryMap.getValue(0xff10), cpu.registers.a)

        cpu.memoryMap.setValue(cpu.registers.programCounter, 0x20)
        cpu.memoryMap.setValue(cpu.registers.programCounter + 1, 0xc0)
        cpu.memoryMap.setValue(0xc020, 0x77)
        test.op0xfa.invoke()
        assertEquals(cpu.registers.a, 0x77)
        cpu.registers.a = 0x03
        cpu.registers.programCounter -= 2
        test.op0xea.invoke()
        assertEquals(cpu.memoryMap.getValue(0xc020), 0x03)

        cpu.registers.h = 0xff
        cpu.registers.l = 0xff
        cpu.memoryMap.setValue(0xffff, 0xaf)
        test.op0x2a.invoke()
        assertEquals(cpu.registers.a, 0xaf)
        cpu.memoryMap.setValue(0x0, 0x22)
        test.op0x3a.invoke()
        assertEquals(cpu.registers.a, 0x22)
        assertEquals(cpu.registers.getHL(), 0xffff)

        cpu.registers.b = 0xca
        cpu.registers.c = 0x02
        cpu.registers.d = 0xcb
        cpu.registers.e = 0x02
        cpu.registers.a = 0xee
        test.op0x02.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getBC()), cpu.registers.a)
        test.op0x12.invoke()
        assertEquals(cpu.memoryMap.getValue(cpu.registers.getDE()), cpu.registers.a)

        cpu.registers.h = 0xcf
        cpu.registers.l = 0xcf
        test.op0x22.invoke()
        assertEquals(cpu.memoryMap.getValue(0xcfcf), cpu.registers.a)
        test.op0x32.invoke()
        assertEquals(cpu.memoryMap.getValue(0xcfd0), cpu.registers.a)
        assertEquals(cpu.registers.getHL(), 0xcfcf)
    }
}
