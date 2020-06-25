// ArithmeticLogical8bit.kt
// Version 1.2
// Implements CPU 8 bit arithmetical and logical instructions

package monkeygb.cpu

class ArithmeticLogical8bit(private val cpu: Cpu) {
    // internal utility functions
    private fun setFlagsAdd(oldResult: Int, newResult: Int) {
        cpu.registers.addSubFlag = false
        cpu.registers.zeroFlag = newResult % 0x100 == 0
        cpu.registers.carryFlag = newResult > 0xff
        cpu.registers.halfCarryFlag = ((oldResult and 0xf) + ((newResult - oldResult) and 0xf))and 0x10 == 0x10
    }
    private fun setFlagsAdc(oldResult: Int, newResult: Int, carryFlagInt: Int) {
        setFlagsAdd(oldResult, newResult)
        cpu.registers.halfCarryFlag = false
        if ((((oldResult and 0xf) + carryFlagInt) + ((newResult - oldResult -carryFlagInt) and 0xf))and 0x10 == 0x10)
            cpu.registers.halfCarryFlag = true
        if (((oldResult and 0xf) + (((newResult - oldResult -carryFlagInt) and 0xf) + carryFlagInt))and 0x10 == 0x10)
            cpu.registers.halfCarryFlag = true
    }
    private fun setFlagsSub(oldResult: Int, newResult: Int) {
        cpu.registers.addSubFlag = true
        cpu.registers.zeroFlag = newResult == 0
        cpu.registers.carryFlag = newResult < 0
        cpu.registers.halfCarryFlag = (oldResult and 0xf)  < (oldResult - newResult) and 0xf
    }
    private fun setFlagsSbc(oldResult: Int, newResult: Int, carryFlagInt: Int) {
        setFlagsSub(oldResult, newResult)
        cpu.registers.halfCarryFlag = false
        if ((oldResult and 0xf)  < (oldResult - newResult - carryFlagInt) and 0xf)
            cpu.registers.halfCarryFlag = true
        if (((oldResult - carryFlagInt) and 0xf)  < (oldResult - newResult - carryFlagInt) and 0xf)
            cpu.registers.halfCarryFlag = true
    }
    private fun setFlagsAnd(result: Int) {
        cpu.registers.zeroFlag = result == 0
        cpu.registers.halfCarryFlag = true
        cpu.registers.carryFlag = false
        cpu.registers.addSubFlag = false
    }
    private fun setFlagsOr(result: Int) {
        cpu.registers.zeroFlag = result == 0
        cpu.registers.halfCarryFlag = false
        cpu.registers.carryFlag = false
        cpu.registers.addSubFlag = false
    }
    private fun setFlagsInc(oldResult: Int, newResult: Int) {
        cpu.registers.addSubFlag = false
        cpu.registers.zeroFlag = newResult == 0
        cpu.registers.halfCarryFlag = oldResult and 0x8 == 0x8 && newResult and 0x8 == 0
    }
    private fun setFlagsDec(oldResult: Int, newResult: Int) {
        cpu.registers.addSubFlag = true
        cpu.registers.zeroFlag = newResult == 0
        cpu.registers.halfCarryFlag = (oldResult and 0xf)  < (oldResult - newResult) and 0xf
    }

    // implementing instruction
    val op0x80 = {      // ADD A, B
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.b)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x81 = {      // ADD A, C
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.c)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x82 = {      // ADD A, D
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.d)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x83 = {      // ADD A, E
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.e)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x84 = {      // ADD A, H
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.h)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x85 = {      // ADD A, L
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.l)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }
    val op0x87 = {      // ADD A, A
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a + cpu.registers.a)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }

    val op0xc6 = {      // ADD A, n
        cpu.machineCycles += 2
        val numberToAdd: Int = cpu.readNextByte()
        val result: Int = (cpu.registers.a + numberToAdd)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result  % 0x100
    }

    val op0x86 = {      // ADD A, (HL)
        cpu.machineCycles += 2
        val numberToAdd: Int = cpu.memoryMap.getValue(cpu.registers.getHL())
        val result: Int = (cpu.registers.a + numberToAdd)
        setFlagsAdd(cpu.registers.a, result)
        cpu.registers.a = result % 0x100
    }

    val op0x88 = {      // ADC A, B
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.b + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x89 = {      // ADC A, C
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.c + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8a = {      // ADC A, D
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.d + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8b = {      // ADC A, E
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.e + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8c = {      // ADC A, H
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.h + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8d = {      // ADC A, L
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.l + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8f = {      // ADC A, A
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + cpu.registers.a + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0xce = {      // ADC A, n
        cpu.machineCycles += 2
        val numberToAdd: Int = cpu.readNextByte()
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + numberToAdd + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }
    val op0x8e = {      // ADC A, (HL)
        cpu.machineCycles += 2
        val numberToAdd: Int = cpu.memoryMap.getValue(cpu.registers.getHL())
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        val result: Int = (cpu.registers.a + numberToAdd + carryFlagInt)
        setFlagsAdc(cpu.registers.a, result, carryFlagInt)
        cpu.registers.a = result % 0x100
    }

    val op0x90 = {      // SUB B
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.b
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x91 = {      // SUB C
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.c
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x92 = {      // SUB D
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.d
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x93 = {      // SUB E
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.e
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x94 = {      // SUB H
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.h
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x95 = {      // SUB L
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.l
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x97 = {      // SUB A
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - cpu.registers.a
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0xd6 = {      // SUB n
        cpu.machineCycles += 2
        var result: Int = cpu.registers.a - cpu.readNextByte()
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x96 = {      // SUB (HL)
        cpu.machineCycles += 2
        var result: Int = cpu.registers.a - cpu.memoryMap.getValue(cpu.registers.getHL())
        setFlagsSub(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }

    val op0x98 = {      // SBC B
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.b - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x99 = {      // SBC C
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.c - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9a = {      // SBC D
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.d - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9b = {      // SBC E
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.e - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9c = {      // SBC H
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.h - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9d = {      // SBC L
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.l - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9f = {      // SBC A
        cpu.machineCycles += 1
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.registers.a - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0xde = {      // SBC n
        cpu.machineCycles += 2
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.readNextByte() - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x9e = {      // SBC (HL)
        cpu.machineCycles += 2
        val carryFlagInt = if (cpu.registers.carryFlag)
            1
        else
            0
        var result: Int = cpu.registers.a - cpu.memoryMap.getValue(cpu.registers.getHL()) - carryFlagInt
        setFlagsSbc(cpu.registers.a, result, carryFlagInt)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }

    val op0xa0 = {      // AND B
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.b
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa1 = {      // AND C
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.c
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa2 = {      // AND D
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.d
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa3 = {      // AND E
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.e
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa4 = {      // AND H
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.h
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa5 = {      // AND L
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.l
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa7 = {      // AND A
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a and cpu.registers.a
        setFlagsAnd(cpu.registers.a)
    }
    val op0xe6 = {     // AND n
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a and cpu.readNextByte()
        setFlagsAnd(cpu.registers.a)
    }
    val op0xa6 = {     // AND (HL)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a and cpu.memoryMap.getValue(cpu.registers.getHL())
        setFlagsAnd(cpu.registers.a)
    }

    val op0xb0 = {      // OR B
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.b
        setFlagsOr(cpu.registers.a)
    }
    val op0xb1 = {      // OR C
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.c
        setFlagsOr(cpu.registers.a)
    }
    val op0xb2 = {      // OR D
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.d
        setFlagsOr(cpu.registers.a)
    }
    val op0xb3 = {      // OR E
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.e
        setFlagsOr(cpu.registers.a)
    }
    val op0xb4 = {      // OR H
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.h
        setFlagsOr(cpu.registers.a)
    }
    val op0xb5 = {      // OR L
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.l
        setFlagsOr(cpu.registers.a)
    }
    val op0xb7 = {      // OR A
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a or cpu.registers.a
        setFlagsOr(cpu.registers.a)
    }
    val op0xf6 = {     // OR n
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a or cpu.readNextByte()
        setFlagsOr(cpu.registers.a)
    }
    val op0xb6 = {     // OR (HL)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a or cpu.memoryMap.getValue(cpu.registers.getHL())
        setFlagsOr(cpu.registers.a)
    }

    val op0xa8 = {      // XOR B
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.b
        setFlagsOr(cpu.registers.a)
    }
    val op0xa9 = {      // XOR C
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.c
        setFlagsOr(cpu.registers.a)
    }
    val op0xaa = {      // XOR D
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.d
        setFlagsOr(cpu.registers.a)
    }
    val op0xab = {      // XOR E
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.e
        setFlagsOr(cpu.registers.a)
    }
    val op0xac = {      // XOR H
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.h
        setFlagsOr(cpu.registers.a)
    }
    val op0xad = {      // XOR L
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.l
        setFlagsOr(cpu.registers.a)
    }
    val op0xaf = {      // XOR A
        cpu.machineCycles += 1
        cpu.registers.a = cpu.registers.a xor cpu.registers.a
        setFlagsOr(cpu.registers.a)
    }
    val op0xee = {     // XOR n
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a xor cpu.readNextByte()
        setFlagsOr(cpu.registers.a)
    }
    val op0xae = {     // XOR (HL)
        cpu.machineCycles += 2
        cpu.registers.a = cpu.registers.a xor cpu.memoryMap.getValue(cpu.registers.getHL())
        setFlagsOr(cpu.registers.a)
    }

    val op0xb8 = {      // CP B
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.b)
    }
    val op0xb9 = {      // CP C
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.c)
    }
    val op0xba = {      // CP D
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.d)
    }
    val op0xbb = {      // CP E
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.e)
    }
    val op0xbc = {      // CP H
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.h)
    }
    val op0xbd = {      // CP L
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.l)
    }
    val op0xbf = {      // CP A
        cpu.machineCycles += 1
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.registers.a)
    }
    val op0xfe = {      // CP n
        cpu.machineCycles += 2
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.readNextByte())
    }
    val op0xbe = {      // CP (HL)
        cpu.machineCycles += 2
        setFlagsSub(cpu.registers.a, cpu.registers.a - cpu.memoryMap.getValue(cpu.registers.getHL()))
    }

    val op0x04 = {      // INC B
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.b +1) % 0x100
        setFlagsInc(cpu.registers.b, result)
        cpu.registers.b = result
    }
    val op0x0c = {      // INC C
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.c +1) % 0x100
        setFlagsInc(cpu.registers.c, result)
        cpu.registers.c = result
    }
    val op0x14 = {      // INC D
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.d +1) % 0x100
        setFlagsInc(cpu.registers.d, result)
        cpu.registers.d = result
    }
    val op0x1c = {      // INC E
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.e +1) % 0x100
        setFlagsInc(cpu.registers.e, result)
        cpu.registers.e = result
    }
    val op0x24 = {      // INC H
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.h +1) % 0x100
        setFlagsInc(cpu.registers.h, result)
        cpu.registers.h = result
    }
    val op0x2c = {      // INC L
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.l +1) % 0x100
        setFlagsInc(cpu.registers.l, result)
        cpu.registers.l = result
    }
    val op0x3c = {      // INC A
        cpu.machineCycles += 1
        val result: Int = (cpu.registers.a +1) % 0x100
        setFlagsInc(cpu.registers.a, result)
        cpu.registers.a = result
    }
    val op0x34 = {      // INC (HL)
        cpu.machineCycles += 3
        val result: Int = (cpu.memoryMap.getValue(cpu.registers.getHL()) +1) % 0x100
        setFlagsInc(cpu.memoryMap.getValue(cpu.registers.getHL()), result)
        cpu.memoryMap.setValue(cpu.registers.getHL(), result)
    }

    val op0x05 = {      // DEC B
        cpu.machineCycles += 1
        var result: Int = cpu.registers.b - 1
        setFlagsDec(cpu.registers.b, result)
        if (result < 0)
            result += 0x100
        cpu.registers.b = result
    }
    val op0x0d = {      // DEC C
        cpu.machineCycles += 1
        var result: Int = cpu.registers.c - 1
        setFlagsDec(cpu.registers.c, result)
        if (result < 0)
            result += 0x100
        cpu.registers.c = result
    }
    val op0x15 = {      // DEC D
        cpu.machineCycles += 1
        var result: Int = cpu.registers.d - 1
        setFlagsDec(cpu.registers.d, result)
        if (result < 0)
            result += 0x100
        cpu.registers.d = result
    }
    val op0x1d = {      // DEC E
        cpu.machineCycles += 1
        var result: Int = cpu.registers.e - 1
        setFlagsDec(cpu.registers.e, result)
        if (result < 0)
            result += 0x100
        cpu.registers.e = result
    }
    val op0x25 = {      // DEC H
        cpu.machineCycles += 1
        var result: Int = cpu.registers.h - 1
        setFlagsDec(cpu.registers.h, result)
        if (result < 0)
            result += 0x100
        cpu.registers.h = result
    }
    val op0x2d = {      // DEC L
        cpu.machineCycles += 1
        var result: Int = cpu.registers.l - 1
        setFlagsDec(cpu.registers.l, result)
        if (result < 0)
            result += 0x100
        cpu.registers.l = result
    }
    val op0x3d= {      // DEC A
        cpu.machineCycles += 1
        var result: Int = cpu.registers.a - 1
        setFlagsDec(cpu.registers.a, result)
        if (result < 0)
            result += 0x100
        cpu.registers.a = result
    }
    val op0x35 = {      // DEC (HL)
        cpu.machineCycles += 3
        var result: Int = cpu.memoryMap.getValue(cpu.registers.getHL()) - 1
        setFlagsDec(cpu.memoryMap.getValue(cpu.registers.getHL()), result)
        if (result < 0)
            result += 0x100
        cpu.memoryMap.setValue(cpu.registers.getHL(), result)
    }
}