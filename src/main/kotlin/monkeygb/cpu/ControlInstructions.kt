// ControlInstruction.kt
// Version 1.1
// Implements CPU control instructions

package monkeygb.cpu

class ControlInstructions(private val cpu: Cpu) {
    // implementing instructions
    val op0x27 = {      // DAA
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
}