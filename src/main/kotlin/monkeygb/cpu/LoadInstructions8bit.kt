// LoadInstruction8bit.kt
// Version 1.2
// Implements CPU 8 bit load instructions

package monkeygb.cpu

class LoadInstructions8bit(private val cpu: Cpu) {
    //instructions implementation
    val op0x40 = {      // LD B, B
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.b
    }
    val op0x41 = {      // LD B, C
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.c
    }
    val op0x42 = {      // LD B, D
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.d
    }
    val op0x43 = {      // LD B, E
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.e
    }
    val op0x44 = {      // LD B, H
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.h
    }
    val op0x45 = {      // LD B, L
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.l
    }
    val op0x47 = {      // LD B, A
        cpu.machineCycles += 1
        cpu.registers.b = cpu.registers.a
    }
    val op0x48 = {      // LD C, B
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.b
    }
    val op0x49 = {      // LD C, C
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.c
    }
    val op0x4a = {      // LD C, D
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.d
    }
    val op0x4b = {      // LD C, E
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.e
    }
    val op0x4c = {      // LD C, H
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.h
    }
    val op0x4d = {      // LD C, L
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.l
    }
    val op0x4f = {      // LD C, A
        cpu.machineCycles += 1
        cpu.registers.c = cpu.registers.a
    }
    val op0x50 = {      // LD D, B
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.b
    }
    val op0x51 = {      // LD D, C
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.c
    }
    val op0x52 = {      // LD D, D
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.d
    }
    val op0x53 = {      // LD D, E
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.e
    }
    val op0x54 = {      // LD D, H
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.h
    }
    val op0x55 = {      // LD D, L
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.l
    }
    val op0x57 = {      // LD D, A
        cpu.machineCycles += 1
        cpu.registers.d = cpu.registers.a
    }
    val op0x58 = {      // LD E, B
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.b
    }
    val op0x59 = {      // LD E, C
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.c
    }
    val op0x5a = {      // LD E, D
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.d
    }
    val op0x5b = {      // LD E, E
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.e
    }
    val op0x5c = {      // LD E, H
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.h
    }
    val op0x5d = {      // LD E, L
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.l
    }
    val op0x5f = {      // LD E, A
        cpu.machineCycles += 1
        cpu.registers.e = cpu.registers.a
    }
    val op0x60 = {      // LD H, B
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.b
    }
    val op0x61 = {      // LD H, C
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.c
    }
    val op0x62 = {      // LD H, D
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.d
    }
    val op0x63 = {      // LD H, E
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.e
    }
    val op0x64 = {      // LD H, H
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.h
    }
    val op0x65 = {      // LD H, L
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.l
    }
    val op0x67 = {      // LD H, A
        cpu.machineCycles += 1
        cpu.registers.h = cpu.registers.a
    }
    val op0x68 = {      // LD L, B
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.b
    }
    val op0x69 = {      // LD L, C
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.c
    }
    val op0x6a = {      // LD L, D
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.d
    }
    val op0x6b = {      // LD L, E
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.e
    }
    val op0x6c = {      // LD L, H
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.h
    }
    val op0x6d = {      // LD L, L
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.l
    }
    val op0x6f = {      // LD L, A
        cpu.machineCycles += 1
        cpu.registers.l = cpu.registers.a
    }
    val op0x78 = {      // LD A, B
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.b
    }
    val op0x79 = {      // LD A, C
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.c
    }
    val op0x7a = {      // LD A, D
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.d
    }
    val op0x7b = {      // LD A, E
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.e
    }
    val op0x7c = {      // LD A, H
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.h
    }
    val op0x7d = {      // LD A, L
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.l
    }
    val op0x7f = {      // LD A, A
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a
    }

    val op0x06 = {      // LD B, n
        cpu.machineCycles += 2
        cpu.registers.b = cpu.readNextByte()
    }
    val op0x0e = {      // LD C, n
        cpu.machineCycles += 2
        cpu.registers.c = cpu.readNextByte()
    }
    val op0x16 = {      // LD D, n
        cpu.machineCycles += 2
        cpu.registers.d = cpu.readNextByte()
    }
    val op0x1e = {      // LD E, n
        cpu.machineCycles += 2
        cpu.registers.e = cpu.readNextByte()
    }
    val op0x26 = {      // LD H, n
        cpu.machineCycles += 2
        cpu.registers.h = cpu.readNextByte()
    }
    val op0x2e = {      // LD L, n
        cpu.machineCycles += 2
        cpu.registers.l = cpu.readNextByte()
    }
    val op0x3e = {      // LD A, n
        cpu.machineCycles += 2
        cpu.registers.a = cpu.readNextByte()
    }

    val op0x46 = {      // LD B, (HL)
        cpu.machineCycles += 2
        cpu.registers.b = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x4e = {      // LD C, (HL)
        cpu.machineCycles += 2
        cpu.registers.c = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x56 = {      // LD D, (HL)
        cpu.machineCycles += 2
        cpu.registers.d = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x5e = {      // LD E, (HL)
        cpu.machineCycles += 2
        cpu.registers.e = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x66 = {      // LD H, (HL)
        cpu.machineCycles += 2
        cpu.registers.h = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x6e = {      // LD L, (HL)
        cpu.machineCycles += 2
        cpu.registers.l = cpu.memoryMap.getValue(cpu.registers.getHL())
    }
    val op0x7e = {      // LD A, (HL)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.getHL())
    }

    val op0x70 = {      // LD (HL), B
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.b)
    }
    val op0x71 = {      // LD (HL), C
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.c)
    }
    val op0x72 = {      // LD (HL), D
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.d)
    }
    val op0x73 = {      // LD (HL), E
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.e)
    }
    val op0x74 = {      // LD (HL), H
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.h)
    }
    val op0x75 = {      // LD (HL), L
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.l)
    }
    val op0x77 = {      // LD (HL), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.a)
    }

    val op0x36 = {      // LD (HL), n
        cpu.machineCycles += 3
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.readNextByte())

    }

    val op0x0a = {      // LD A, (BC)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.getBC())
    }

    val op0x1a = {      // LD A, (DE)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.getDE())
    }

    val op0xf2 = {      // LD A, (C)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.c + 0xff00)
    }

    val op0xe2 = {      // LD (C), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.c + 0xff00, cpu.registers.a)
    }

    val op0xf0 = {      // LD A, (n)
        cpu.machineCycles += 3
        cpu.registers.a = cpu.memoryMap.getValue(0xff00 + cpu.readNextByte())
    }

    val op0xe0 = {      // LD (n), A
        cpu.machineCycles += 3
        cpu.memoryMap.setValue(0xff00 + cpu.readNextByte(), cpu.registers.a)
    }

    val op0xfa = {      // LD A, (nn)
        cpu.machineCycles += 4
        val lowByte: Int = cpu.readNextByte()
        val highByte: Int = cpu.readNextByte()
        cpu.registers.a = cpu.memoryMap.getValue((highByte shl 8) + lowByte)
    }

    val op0xea = {      // LD (nn), A
        cpu.machineCycles += 4
        val lowByte: Int = cpu.readNextByte()
        val highByte: Int = cpu.readNextByte()
        cpu.memoryMap.setValue((highByte shl 8) + lowByte, cpu.registers.a)
    }

    val op0x2a = {      // LD A, (HLI)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.getHL())
        cpu.registers.setHL((cpu.registers.getHL() +1) %0x10000)
    }

    val op0x3a = {      // LD A, (HLD)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.getHL())
        cpu.registers.setHL((cpu.registers.getHL() -1))
        if (cpu.registers.getHL() < 0)
            cpu.registers.setHL(0xffff)
    }

    val op0x02 = {      // LD (BC), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getBC(), cpu.registers.a)
    }

    val op0x12 = {      // LD (DE), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getDE(), cpu.registers.a)
    }

    val op0x22 = {      // LD (HLI), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.a)
        cpu.registers.setHL((cpu.registers.getHL() +1) %0x10000)
    }

    val op0x32 = {      // LD (HLD), A
        cpu.machineCycles += 2
        cpu.memoryMap.setValue(cpu.registers.getHL(), cpu.registers.a)
        cpu.registers.setHL((cpu.registers.getHL() -1))
        if (cpu.registers.getHL() < 0)
            cpu.registers.setHL(0xffff)
    }
}