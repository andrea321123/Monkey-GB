// LoadInstruction16bit.kt
// Version 1.3
// Implements CPU 16 bit load instructions

package monkeygb.cpu

class LoadInstructions16bit(private val cpu: Cpu) {
    //instruction implementations
    val op0x01 = {      // LD BC, nn
        cpu.machineCycles += 3
        cpu.registers.c = cpu.readNextByte()
        cpu.registers.b = cpu.readNextByte()
    }
    val op0x11 = {      // LD DE, nn
        cpu.machineCycles += 3
        cpu.registers.e = cpu.readNextByte()
        cpu.registers.d = cpu.readNextByte()
    }
    val op0x21 = {      // LD HL, nn
        cpu.machineCycles += 3
        cpu.registers.l = cpu.readNextByte()
        cpu.registers.h = cpu.readNextByte()
    }
    val op0x31 = {      // LD SP, nn
        cpu.machineCycles += 3
        val lowByte: Int = cpu.readNextByte()
        val highByte: Int = cpu.readNextByte()
        cpu.registers.stackPointer = (highByte shl 8) + lowByte
    }

    val op0xf9 = {      // LD SP, HL
        cpu.machineCycles += 2
        cpu.registers.stackPointer = cpu.registers.getHL()
    }

    val op0xc5 = {      // PUSH BC
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.b)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.c)
        cpu.registers.stackPointer -= 2
    }
    val op0xd5 = {      // PUSH DE
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.d)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.e)
        cpu.registers.stackPointer -= 2
    }
    val op0xe5 = {      // PUSH HL
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.h)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.l)
        cpu.registers.stackPointer -= 2
    }
    val op0xf5 = {      // PUSH AF
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.a)
        var flagRegister = 0
        // creating an int number from flags to be pushed onto the stack
        if (cpu.registers.zeroFlag)
            flagRegister = flagRegister or 0b10000000
        if (cpu.registers.addSubFlag)
            flagRegister = flagRegister or 0b01000000
        if (cpu.registers.halfCarryFlag)
            flagRegister = flagRegister or 0b00100000
        if (cpu.registers.carryFlag)
            flagRegister = flagRegister or 0b00010000
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, flagRegister)
        cpu.registers.stackPointer -= 2
    }

    val op0xc1 = {      // POP BC
        cpu.machineCycles += 3
        cpu.registers.c = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.b = cpu.memoryMap.getValue(cpu.registers.stackPointer +1)
        cpu.registers.stackPointer += 2
    }
    val op0xd1 = {      // POP DE
        cpu.machineCycles += 3
        cpu.registers.e = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.d = cpu.memoryMap.getValue(cpu.registers.stackPointer +1)
        cpu.registers.stackPointer += 2
    }
    val op0xe1 = {      // POP HL
        cpu.machineCycles += 3
        cpu.registers.l = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.h = cpu.memoryMap.getValue(cpu.registers.stackPointer +1)
        cpu.registers.stackPointer += 2
    }
    val op0xf1 = {      // POP AF
        cpu.machineCycles += 3

        // updating flag registers from int number
        val flagRegister: Int = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.zeroFlag = flagRegister and 0b10000000 == 0b10000000
        cpu.registers.addSubFlag = flagRegister and 0b01000000 == 0b01000000
        cpu.registers.halfCarryFlag = flagRegister and 0b00100000 == 0b00100000
        cpu.registers.carryFlag = flagRegister and 0b00010000 == 0b00010000
        cpu.registers.a = cpu.memoryMap.getValue(cpu.registers.stackPointer +1)
        cpu.registers.stackPointer += 2
    }

    val op0xf8 = {      // LD HL, SP + e
        cpu.machineCycles += 3

        // e is a signed number in 2's complements
        val e = cpu.readNextByte()
        val negativeE = -(e and 0b10000000)
        val positiveE = e and 0b01111111
        val number = positiveE + negativeE
        var result = cpu.registers.stackPointer + number

        // setting flags
        cpu.registers.halfCarryFlag = result and 0b00010000 == 0b00010000

        if (result > 0xffff || result < 0) {
            cpu.registers.carryFlag = true
            if (result > 0xff)
                result %= 0xff
            if(result < 0)
                result += 0xff
        }
        else
            cpu.registers.carryFlag = false

        cpu.registers.addSubFlag = false
        cpu.registers.zeroFlag = false
        cpu.registers.stackPointer = result
    }

    val op0x08 = {      // LD (nn), SP
        cpu.machineCycles += 5
        val lowByte: Int = cpu.readNextByte()
        val highByte: Int = cpu.readNextByte()
        val address: Int = (highByte shl 8) + lowByte
        cpu.memoryMap.setValue(address, cpu.registers.stackPointer and 0xff)
        cpu.memoryMap.setValue(address +1, cpu.registers.stackPointer shr 8)
    }
}