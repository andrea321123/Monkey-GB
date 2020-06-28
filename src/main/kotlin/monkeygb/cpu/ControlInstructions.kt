// ControlInstruction.kt
// Version 1.2
// Implements CPU control instructions

package monkeygb.cpu

class ControlInstructions(private val cpu: Cpu) {
    // implementing instructions
    val op0x27 = {      // DAA
        cpu.machineCycles += 1
        var result = cpu.registers.a

        // after an addition
        if (!cpu.registers.addSubFlag) {
            if (cpu.registers.halfCarryFlag || (result and 0xf) > 9)
                result += 0x06
            if (cpu.registers.carryFlag || (result > 0x9f))
                result += 0x60
        }
        // after a subtraction
        else {
            if (cpu.registers.halfCarryFlag)
                result = (result - 0x06) and 0xff
            if (cpu.registers.carryFlag || (result > 0x9f))
                result -= 0x60
        }

        cpu.registers.halfCarryFlag = false

        // carry flag
        if ((result and 0x100) == 0x100)
            cpu.registers.carryFlag = true
        result = result and 0xff

        // zero flag
        cpu.registers.zeroFlag = result == 0

        cpu.registers.a = result
    }

    val op0x2f = {      // CPL
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor 0xff
    }

    val op0x00 = {      // NOP
        cpu.machineCycles += 1
    }

    val op0x3f = {      // CCF
        cpu.machineCycles += 1
        cpu.registers.carryFlag = !cpu.registers.carryFlag
    }
    val op0x37 = {      // SCF
        cpu.machineCycles += 1
        cpu.registers.carryFlag = true
    }

    val op0xf3 = {      // DI
        cpu.machineCycles += 1
        cpu.ime = false
    }
    val op0xfb = {      // EI
        cpu.machineCycles += 1
        cpu.ime = true
    }

    val op0x76 = {      // HALT
        cpu.machineCycles += 1
        cpu.haltMode = true
    }

    val op0x10 = {      // STOP
        cpu.machineCycles += 1
        val empty = cpu.readNextByte()      // Should be 0x00
        cpu.stopMode = true
    }
}