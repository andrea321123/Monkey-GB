// JumpCallInstructions.kt
// Version 1.5
// Implements CPU jump and call instructions

package monkeygb.cpu

import monkeygb.complement2toInt

class JumpCallsInstructions(private val cpu: Cpu) {
    // implementing instructions
    val op0xc3 = {      // JP nn
        cpu.machineCycles += 4
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        cpu.registers.programCounter = lowByte + (highByte shl 8)
    }

    val op0xc2 = {      // JP NZ, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (!cpu.registers.zeroFlag) {
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.machineCycles += 1
        }
    }
    val op0xca = {      // JP Z, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (cpu.registers.zeroFlag) {
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.machineCycles += 1
        }
    }
    val op0xd2 = {      // JP NC, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (!cpu.registers.carryFlag) {
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.machineCycles += 1
        }
    }
    val op0xda = {      // JP C, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (cpu.registers.carryFlag) {
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.machineCycles += 1
        }
    }

    val op0x18 = {      // JR e
        cpu.machineCycles += 3

        // e is a signed number in 2's complements
        val e = complement2toInt(cpu.readNextByte())
        cpu.registers.programCounter += e
    }

    val op0x20 = {      // JR NZ, e
        cpu.machineCycles += 2

        // e is a signed number in 2's complements
        val e = complement2toInt(cpu.readNextByte())
        if (!cpu.registers.zeroFlag) {
            cpu.registers.programCounter += e
            cpu.machineCycles += 1
        }
    }
    val op0x28 = {      // JR Z, e
        cpu.machineCycles += 2

        // e is a signed number in 2's complements
        val e = complement2toInt(cpu.readNextByte())
        if (cpu.registers.zeroFlag) {
            cpu.registers.programCounter += e
            cpu.machineCycles += 1
        }
    }
    val op0x30 = {      // JR NC, e
        cpu.machineCycles += 2

        // e is a signed number in 2's complements
        val e = complement2toInt(cpu.readNextByte())
        if (!cpu.registers.carryFlag) {
            cpu.registers.programCounter += e
            cpu.machineCycles += 1
        }
    }
    val op0x38 = {      // JR C, e
        cpu.machineCycles += 2

        // e is a signed number in 2's complements
        val e = complement2toInt(cpu.readNextByte())
        if (cpu.registers.carryFlag) {
            cpu.registers.programCounter += e
            cpu.machineCycles += 1
        }
    }

    val op0xe9 = {      // JP (HL)
        cpu.machineCycles += 1
        cpu.registers.programCounter = cpu.registers.getHL()
    }

    val op0xcd = {      // CALL nn
        cpu.machineCycles += 6
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.programCounter = lowByte + (highByte shl 8)
        cpu.registers.stackPointer -= 2
    }
    val op0xc4 = {      // CALL NZ, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (!cpu.registers.zeroFlag) {
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 1, cpu.registers.programCounter shr 8)
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 2, cpu.registers.programCounter and 0xff)
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.registers.stackPointer -= 2
            cpu.machineCycles += 3
        }
    }
    val op0xcc = {      // CALL Z, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (cpu.registers.zeroFlag) {
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 1, cpu.registers.programCounter shr 8)
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 2, cpu.registers.programCounter and 0xff)
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.registers.stackPointer -= 2
            cpu.machineCycles += 3
        }
    }
    val op0xd4 = {      // CALL NC, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (!cpu.registers.carryFlag) {
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 1, cpu.registers.programCounter shr 8)
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 2, cpu.registers.programCounter and 0xff)
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.registers.stackPointer -= 2
            cpu.machineCycles += 3
        }
    }
    val op0xdc = {      // CALL C, nn
        cpu.machineCycles += 3
        val lowByte = cpu.readNextByte()
        val highByte = cpu.readNextByte()
        if (cpu.registers.carryFlag) {
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 1, cpu.registers.programCounter shr 8)
            cpu.memoryMap.setValue(cpu.registers.stackPointer - 2, cpu.registers.programCounter and 0xff)
            cpu.registers.programCounter = lowByte + (highByte shl 8)
            cpu.registers.stackPointer -= 2
            cpu.machineCycles += 3
        }
    }

    val op0xc9 = {      // RET
        cpu.machineCycles += 4
        cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer +1) shl 8
        cpu.registers.stackPointer += 2
    }
    val op0xd9 = {      // RETI
        cpu.machineCycles += 4
        cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
        cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer +1) shl 8
        cpu.registers.stackPointer += 2

        // master interrupt enable flag is returned to its pre-interrupt status
        cpu.ime = true
    }

    val op0xc0 = {      // RET NZ
        cpu.machineCycles += 2
        if (!cpu.registers.zeroFlag) {
            cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
            cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer + 1) shl 8
            cpu.registers.stackPointer += 2
            cpu.machineCycles += 3
        }
    }
    val op0xc8 = {      // RET Z
        cpu.machineCycles += 2
        if (cpu.registers.zeroFlag) {
            cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
            cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer + 1) shl 8
            cpu.registers.stackPointer += 2
            cpu.machineCycles += 3
        }
    }
    val op0xd0 = {      // RET NC
        cpu.machineCycles += 2
        if (!cpu.registers.carryFlag) {
            cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
            cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer + 1) shl 8
            cpu.registers.stackPointer += 2
            cpu.machineCycles += 3
        }
    }
    val op0xd8 = {      // RET C
        cpu.machineCycles += 2
        if (cpu.registers.carryFlag) {
            cpu.registers.programCounter = cpu.memoryMap.getValue(cpu.registers.stackPointer)
            cpu.registers.programCounter += cpu.memoryMap.getValue(cpu.registers.stackPointer + 1) shl 8
            cpu.registers.stackPointer += 2
            cpu.machineCycles += 3
        }
    }

    val op0xc7 = {      // RST 0x00
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x0
    }
    val op0xcf = {      // RST 0x08
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x08
    }
    val op0xd7 = {      // RST 0x10
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x10
    }
    val op0xdf = {      // RST 0x18
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x18
    }
    val op0xe7 = {      // RST 0x20
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x20
    }
    val op0xef = {      // RST 0x28
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x28
    }
    val op0xf7 = {      // RST 0x30
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x30
    }
    val op0xff = {      // RST 0x38
        cpu.machineCycles += 4
        cpu.memoryMap.setValue(cpu.registers.stackPointer -1, cpu.registers.programCounter shr 8)
        cpu.memoryMap.setValue(cpu.registers.stackPointer -2, cpu.registers.programCounter and 0xff)
        cpu.registers.stackPointer -= 2
        cpu.registers.programCounter = 0x38
    }
}