// ArithmeticLogical8bit.kt
// Version 1.1
// Implements CPU 8 bit rotation, shift and bit instructions

package monkeygb.cpu

class RotationShiftBitInstructions(private val cpu: Cpu) {
    // internal utility functions

    // reads bit 0, 1 and 2 from instruction and get the register represented by that number
    private fun getRegisterValue(instruction: Int): Int = when {
        instruction and 0x07 == 0x0 || instruction and 0x07 == 0x8 -> cpu.registers.b
        instruction and 0x07 == 0x1 || instruction and 0x07 == 0x9 -> cpu.registers.c
        instruction and 0x07 == 0x2 || instruction and 0x07 == 0xa -> cpu.registers.d
        instruction and 0x07 == 0x3 || instruction and 0x07 == 0xb -> cpu.registers.e
        instruction and 0x07 == 0x4 || instruction and 0x07 == 0xc -> cpu.registers.h
        instruction and 0x07 == 0x5 || instruction and 0x07 == 0xd -> cpu.registers.l
        instruction and 0x07 == 0x7 || instruction and 0x07 == 0xf -> cpu.registers.a
        instruction and 0x07 == 0x6 || instruction and 0x07 == 0xe -> cpu.memoryMap.getValue(cpu.registers.getHL())

        else -> -1
    }

    // add to cpu.machineCycle the cycles used by the instruction
    private fun addMachineCycles(instruction: Int) {
        if (instruction and 0x07 == 0x6 || instruction and 0x07 == 0xe)
            cpu.machineCycles += 4
        else
            cpu.machineCycles += 2
    }

    // set the value parameter to the proper register obtained reading bit 0, 1 and 2 from instruction
    private fun setValueRegister(instruction: Int, value: Int) {
        when {
            instruction and 0x07 == 0x0 || instruction and 0x07 == 0x8 -> cpu.registers.b = value
            instruction and 0x07 == 0x1 || instruction and 0x07 == 0x9 -> cpu.registers.c = value
            instruction and 0x07 == 0x2 || instruction and 0x07 == 0xa -> cpu.registers.d = value
            instruction and 0x07 == 0x3 || instruction and 0x07 == 0xb -> cpu.registers.e = value
            instruction and 0x07 == 0x4 || instruction and 0x07 == 0xc -> cpu.registers.h = value
            instruction and 0x07 == 0x5 || instruction and 0x07 == 0xd -> cpu.registers.l = value
            instruction and 0x07 == 0x7 || instruction and 0x07 == 0xf -> cpu.registers.a = value
            instruction and 0x07 == 0x6 || instruction and 0x07 == 0xe -> cpu.memoryMap.setValue(cpu.registers.getHL(), value)
        }
    }

    //get the n bit from int value in boolean type
    private fun nBit(value: Int, n: Int): Boolean = when((value shr n) and 0x1) {
        1 -> true
        0 -> false
        else -> false
    }

    // instructions general implementation
    fun rlc(instruction: Int) {
        addMachineCycles(instruction)
        val valueToShift = getRegisterValue(instruction)
        val msb = valueToShift shr 7
        val value = ((valueToShift shl 1) + msb) % 0x100
        setValueRegister(instruction, value)
        cpu.registers.zeroFlag = value == 0
        cpu.registers.carryFlag = msb == 1
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun rl(instruction: Int) {
        addMachineCycles(instruction)
        val valueToShift = getRegisterValue(instruction)
        val carryInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val value = ((valueToShift shl 1) + carryInt) % 0x100
        setValueRegister(instruction, value)
        cpu.registers.zeroFlag = value == 0
        cpu.registers.carryFlag = valueToShift shr 7 == 1
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun rrc(instruction: Int) {
        addMachineCycles(instruction)
        val valueToShift = getRegisterValue(instruction)
        val lsb = (valueToShift shl 7) % 0x100
        val value = (valueToShift shr 1) or lsb
        setValueRegister(instruction, value)
        cpu.registers.zeroFlag = value == 0
        cpu.registers.carryFlag = lsb == 0x80
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun rr(instruction: Int) {
        addMachineCycles(instruction)
        val valueToShift = getRegisterValue(instruction)
        val carryInt = if (cpu.registers.carryFlag)
            0x80
        else
            0
        val value = (valueToShift shr 1) + carryInt
        setValueRegister(instruction, value)
        cpu.registers.zeroFlag = value == 0
        cpu.registers.carryFlag = valueToShift shl 7 == 0x80
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun sla(instruction: Int) {
        // same as rlc but reset bit 0
        rlc(instruction)
        val newValue = getRegisterValue(instruction)
        setValueRegister(instruction, newValue and 0xfe)
        cpu.registers.zeroFlag = (newValue and 0xfe) == 0
    }
    fun sra(instruction: Int) {
        addMachineCycles(instruction)
        val valueToShift = getRegisterValue(instruction)
        val bit7 = (valueToShift and 0x80)
        val value = (valueToShift shr 1) + bit7
        setValueRegister(instruction, value)
        cpu.registers.zeroFlag = value == 0
        cpu.registers.carryFlag = valueToShift shl 7 == 0x80
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun srl(instruction: Int) {
        // same as rrc but reset bit 7
        rrc(instruction)
        val newValue = getRegisterValue(instruction)
        setValueRegister(instruction, newValue and 0x7f)
        cpu.registers.zeroFlag = (newValue and 0x7f) == 0
    }
    fun swap(instruction: Int) {
        addMachineCycles(instruction)
        val valueToSwap = getRegisterValue(instruction)
        val lowNibble = (valueToSwap and 0xf) shl 4
        val highNibble = (valueToSwap and 0xf0) shr 4
        setValueRegister(instruction, lowNibble + highNibble)
        cpu.registers.zeroFlag = (lowNibble + highNibble) == 0
        cpu.registers.carryFlag = false
        cpu.registers.halfCarryFlag = false
        cpu.registers.addSubFlag = false
    }
    fun bit(instruction: Int) {
        addMachineCycles(instruction)
        val value = getRegisterValue(instruction)
        val nBit = (instruction and 0b00111000) shr 3
        cpu.registers.zeroFlag = !nBit(value, nBit)
        cpu.registers.addSubFlag = false
        cpu.registers.halfCarryFlag = true
    }
    fun set(instruction: Int) {
        addMachineCycles(instruction)
        var value = getRegisterValue(instruction)
        when ((instruction and 0b00111000) shr 3) {
            0 -> value = value or 0b00000001
            1 -> value = value or 0b00000010
            2 -> value = value or 0b00000100
            3 -> value = value or 0b00001000
            4 -> value = value or 0b00010000
            5 -> value = value or 0b00100000
            6 -> value = value or 0b01000000
            7 -> value = value or 0b10000000
        }
        setValueRegister(instruction, value)
    }
    fun res(instruction: Int) {
        addMachineCycles(instruction)
        var value = getRegisterValue(instruction)
        when ((instruction and 0b00111000) shr 3) {
            0 -> value = value and 0b11111110
            1 -> value = value and 0b11111101
            2 -> value = value and 0b11111011
            3 -> value = value and 0b11110111
            4 -> value = value and 0b11101111
            5 -> value = value and 0b11011111
            6 -> value = value and 0b10111111
            7 -> value = value and 0b01111111
        }
        setValueRegister(instruction, value)
    }

    // implementing instruction
    val op0xcb = {      // prefix CB
        val opcode = cpu.readNextByte()
        when {
            opcode and 0b11111000 == 0b00000000 -> rlc(opcode)
            opcode and 0b11111000 == 0b00010000 -> rl(opcode)
            opcode and 0b11111000 == 0b00001000 -> rrc(opcode)
            opcode and 0b11111000 == 0b00011000 -> rr(opcode)
            opcode and 0b11111000 == 0b00100000 -> sla(opcode)
            opcode and 0b11111000 == 0b00101000 -> sra(opcode)
            opcode and 0b11111000 == 0b00111000 -> srl(opcode)
            opcode and 0b11111000 == 0b00110000 -> swap(opcode)
            opcode and 0b11000000 == 0b01000000 -> bit(opcode)
            opcode and 0b11000000 == 0b11000000 -> set(opcode)
            opcode and 0b11000000 == 0b10000000 -> res(opcode)
        }
    }
    val op0x07 = {      // RLCA
        rlc(0x07)       // rlc a
        cpu.registers.zeroFlag = false
        cpu.machineCycles -= 1
    }
    val op0x17 = {      // RLA
        rl(0x17)        // rl a
        cpu.registers.zeroFlag = false
        cpu.machineCycles -= 1
    }
    val op0x0f = {      // RRCA
        rrc(0x0f)       // rrc a
        cpu.registers.zeroFlag = false
        cpu.machineCycles -= 1
    }
    val op0x1f = {      // RRA
        rr(0x1f)        // rr a
        cpu.registers.zeroFlag = false
        cpu.machineCycles -= 1
    }
}