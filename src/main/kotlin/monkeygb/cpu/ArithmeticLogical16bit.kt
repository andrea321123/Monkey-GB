// ArithmeticLogical8bit.kt
// Version 1.1
// Implements CPU 16 bit arithmetical and logical instructions

package monkeygb.cpu

class ArithmeticLogical16bit(private val cpu: Cpu) {
    // internal utility functions
    fun setFlagsAdd(oldResult: Int, newResult: Int){
        cpu.registers.addSubFlag = false
        cpu.registers.carryFlag = newResult > 0xffff
        cpu.registers.halfCarryFlag = ((oldResult and 0xfff) + ((newResult - oldResult) and 0xfff))and 0x1000 == 0x1000
    }

    // implementing instructions
    val op0x09 = {      // ADD HL, BC
        cpu.machineCycles += 2
        var result: Int = cpu.registers.getHL() + cpu.registers.getBC()
        setFlagsAdd(cpu.registers.getHL(), result)
        cpu.registers.setHL(result % 0x10000)
    }
    val op0x19 = {      // ADD HL, DE
        cpu.machineCycles += 2
        var result: Int = cpu.registers.getHL() + cpu.registers.getDE()
        setFlagsAdd(cpu.registers.getHL(), result)
        cpu.registers.setHL(result % 0x10000)
    }
    val op0x29 = {      // ADD HL, HL
        cpu.machineCycles += 2
        var result: Int = cpu.registers.getHL() + cpu.registers.getHL()
        setFlagsAdd(cpu.registers.getHL(), result)
        cpu.registers.setHL(result % 0x10000)
    }
    val op0x39 = {      // ADD HL, SP
        cpu.machineCycles += 2
        var result: Int = cpu.registers.getHL() + cpu.registers.stackPointer
        setFlagsAdd(cpu.registers.getHL(), result)
        cpu.registers.setHL(result % 0x10000)
    }

    val op0xe8 = {      // ADD SP, e
        cpu.machineCycles += 4
        val e = cpu.readNextByte()
        val negativeE = -(e and 0b10000000)
        val positiveE = e and 0b01111111
        val number = positiveE + negativeE
        var result = cpu.registers.stackPointer + number
        setFlagsAdd(cpu.registers.stackPointer, result)
        cpu.registers.zeroFlag = false
        cpu.registers.stackPointer = result
    }

    val op0x03 = {      // INC BC
        cpu.machineCycles += 2
        cpu.registers.setBC((cpu.registers.getBC() +1) % 0x10000)
    }
    val op0x13 = {      // INC DE
        cpu.machineCycles += 2
        cpu.registers.setDE((cpu.registers.getDE() +1) % 0x10000)
    }
    val op0x23 = {      // INC HL
        cpu.machineCycles += 2
        cpu.registers.setHL((cpu.registers.getHL() +1) % 0x10000)
    }
    val op0x33 = {      // INC SP
        cpu.machineCycles += 2
        cpu.registers.stackPointer = (cpu.registers.stackPointer +1) % 0x10000
    }

    val op0x0b = {      // DEC BC
        cpu.machineCycles += 2
        cpu.registers.setBC(cpu.registers.getBC() -1)
        if (cpu.registers.getBC() < 0)
            cpu.registers.setBC(cpu.registers.getBC() + 0x10000)
    }
    val op0x1b = {      // DEC DE
        cpu.machineCycles += 2
        cpu.registers.setDE(cpu.registers.getDE() -1)
        if (cpu.registers.getDE() < 0)
            cpu.registers.setDE(cpu.registers.getDE() + 0x10000)
    }
    val op0x2b = {      // DEC HL
        cpu.machineCycles += 2
        cpu.registers.setHL(cpu.registers.getHL() -1)
        if (cpu.registers.getHL() < 0)
            cpu.registers.setHL(cpu.registers.getHL() + 0x10000)
    }
    val op0x3b = {      // DEC SP
        cpu.machineCycles += 2
        cpu.registers.stackPointer--
        if (cpu.registers.stackPointer < 0)
            cpu.registers.stackPointer += 0x10000
    }
}