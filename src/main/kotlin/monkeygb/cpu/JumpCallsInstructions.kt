// JumpCallInstructions.kt
// Version 1.1
// Implements CPU jump and call instructions

package monkeygb.cpu

import monkeygb.complement2toInt

class JumpCallsInstructions(private val cpu: Cpu) {
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
}