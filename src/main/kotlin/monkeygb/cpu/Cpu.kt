// Cpu.kt
// Version 1.6
// Emulates the GameBoy CPU (SHARP LR35902)

package monkeygb.cpu

import monkeygb.memory.MemoryMap
import monkeygb.registers.Registers
import kotlin.system.exitProcess

class Cpu {

    private val opcodes = mutableMapOf<Int, () -> Unit>()   // key: instruction opcode, value: instruction implementation
    val memoryMap = MemoryMap()
    val registers = Registers()

    var machineCycles: Long = 0     // number of machine cycles executed; frequency: 1 MHz

    // cpu modes
    var haltMode: Boolean = false
    var stopMode: Boolean = false
    var doubleSpeedMode: Boolean = false

    var ime: Boolean = false       // interrupt master enable flag

    // instructions implementations are in their own category-specific class
    private val control =  ControlInstructions(this)
    private val jump = JumpCallsInstructions(this)
    private val load8 = LoadInstructions8bit(this)
    private val load16 = LoadInstructions16bit(this)
    private val arithmetic8 = ArithmeticLogical8bit(this)
    private val arithmetic16 = ArithmeticLogical16bit(this)
    private val rotation = RotationShiftBitInstructions(this)

    init {
        initOpcodes()
    }

    fun executeInstruction() {
        opcodes[readNextByte()]?.invoke()
    }

    fun afterBootRom() {
        registers.programCounter = 0x100
        registers.a = 1
        registers.zeroFlag = true
        registers.halfCarryFlag = true
        registers.carryFlag = true
        registers.setBC(0x0013)
        registers.setDE(0x00d8)
        registers.setHL(0x014d)
        registers.stackPointer = 0xfffe
        memoryMap.setValue(0xff05, 0x0)
        memoryMap.setValue(0xff06, 0x0)
        memoryMap.setValue(0xff07, 0x0)
        memoryMap.setValue(0xff10, 0x80)
        memoryMap.setValue(0xff11, 0xbf)
        memoryMap.setValue(0xff12, 0xf3)
        memoryMap.setValue(0xff14, 0xbf)
        memoryMap.setValue(0xff16, 0x3f)
        memoryMap.setValue(0xff17, 0x0)
        memoryMap.setValue(0xff19, 0xbf)
        memoryMap.setValue(0xff1a, 0x7f)
        memoryMap.setValue(0xff1b, 0xff)
        memoryMap.setValue(0xff1c, 0x9f)
        memoryMap.setValue(0xff1e, 0xbf)
        memoryMap.setValue(0xff20, 0xff)
        memoryMap.setValue(0xff21, 0x0)
        memoryMap.setValue(0xff22, 0x0)
        memoryMap.setValue(0xff23, 0xbf)
        memoryMap.setValue(0xff24, 0x77)
        memoryMap.setValue(0xff25, 0xf3)
        memoryMap.setValue(0xff26, 0xf1)
        memoryMap.setValue(0xff40, 0x91)
        memoryMap.setValue(0xff42, 0x0)
        memoryMap.setValue(0xff43, 0x0)
        memoryMap.setValue(0xff45, 0x0)
        memoryMap.setValue(0xff47, 0xfc)
        memoryMap.setValue(0xff48, 0xff)
        memoryMap.setValue(0xff49, 0xff)
        memoryMap.setValue(0xff4a, 0x0)
        memoryMap.setValue(0xff4b, 0x0)
        memoryMap.setValue(0xffff, 0x0)














    }
    private fun initOpcodes() {
        opcodes[0x00] = control.op0x00
        opcodes[0x10] = control.op0x10
        opcodes[0x27] = control.op0x27
        opcodes[0x2f] = control.op0x2f
        opcodes[0x37] = control.op0x37
        opcodes[0x3f] = control.op0x3f
        opcodes[0x76] = control.op0x76
        opcodes[0xf3] = control.op0xf3
        opcodes[0xfb] = control.op0xfb

        opcodes[0x18] = jump.op0x18
        opcodes[0x20] = jump.op0x20
        opcodes[0x28] = jump.op0x28
        opcodes[0x30] = jump.op0x30
        opcodes[0x38] = jump.op0x38
        opcodes[0xc0] = jump.op0xc0
        opcodes[0xc2] = jump.op0xc2
        opcodes[0xc3] = jump.op0xc3
        opcodes[0xc4] = jump.op0xc4
        opcodes[0xc7] = jump.op0xc7
        opcodes[0xc8] = jump.op0xc8
        opcodes[0xc9] = jump.op0xc9
        opcodes[0xca] = jump.op0xca
        opcodes[0xcc] = jump.op0xcc
        opcodes[0xcd] = jump.op0xcd
        opcodes[0xcf] = jump.op0xcf
        opcodes[0xd0] = jump.op0xd0
        opcodes[0xd2] = jump.op0xd2
        opcodes[0xd4] = jump.op0xd4
        opcodes[0xd7] = jump.op0xd7
        opcodes[0xd8] = jump.op0xd8
        opcodes[0xd9] = jump.op0xd9
        opcodes[0xda] = jump.op0xda
        opcodes[0xdc] = jump.op0xdc
        opcodes[0xdf] = jump.op0xdf
        opcodes[0xe7] = jump.op0xe7
        opcodes[0xe9] = jump.op0xe9
        opcodes[0xef] = jump.op0xef
        opcodes[0xf7] = jump.op0xf7
        opcodes[0xff] = jump.op0xff

        opcodes[0x40] = load8.op0x40
        opcodes[0x41] = load8.op0x41
        opcodes[0x42] = load8.op0x42
        opcodes[0x43] = load8.op0x43
        opcodes[0x44] = load8.op0x44
        opcodes[0x45] = load8.op0x45
        opcodes[0x46] = load8.op0x46
        opcodes[0x47] = load8.op0x47
        opcodes[0x48] = load8.op0x48
        opcodes[0x49] = load8.op0x49
        opcodes[0x4a] = load8.op0x4a
        opcodes[0x4b] = load8.op0x4b
        opcodes[0x4c] = load8.op0x4c
        opcodes[0x4d] = load8.op0x4d
        opcodes[0x4e] = load8.op0x4e
        opcodes[0x4f] = load8.op0x4f
        opcodes[0x50] = load8.op0x50
        opcodes[0x51] = load8.op0x51
        opcodes[0x52] = load8.op0x52
        opcodes[0x53] = load8.op0x53
        opcodes[0x54] = load8.op0x54
        opcodes[0x55] = load8.op0x55
        opcodes[0x56] = load8.op0x56
        opcodes[0x57] = load8.op0x57
        opcodes[0x58] = load8.op0x58
        opcodes[0x59] = load8.op0x59
        opcodes[0x5a] = load8.op0x5a
        opcodes[0x5b] = load8.op0x5b
        opcodes[0x5c] = load8.op0x5c
        opcodes[0x5d] = load8.op0x5d
        opcodes[0x5e] = load8.op0x5e
        opcodes[0x5f] = load8.op0x5f
        opcodes[0x60] = load8.op0x60
        opcodes[0x61] = load8.op0x61
        opcodes[0x62] = load8.op0x62
        opcodes[0x63] = load8.op0x63
        opcodes[0x64] = load8.op0x64
        opcodes[0x65] = load8.op0x65
        opcodes[0x66] = load8.op0x66
        opcodes[0x67] = load8.op0x67
        opcodes[0x68] = load8.op0x68
        opcodes[0x69] = load8.op0x69
        opcodes[0x6a] = load8.op0x6a
        opcodes[0x6b] = load8.op0x6b
        opcodes[0x6c] = load8.op0x6c
        opcodes[0x6d] = load8.op0x6d
        opcodes[0x6e] = load8.op0x6e
        opcodes[0x6f] = load8.op0x6f
        opcodes[0x70] = load8.op0x70
        opcodes[0x71] = load8.op0x71
        opcodes[0x72] = load8.op0x72
        opcodes[0x73] = load8.op0x73
        opcodes[0x74] = load8.op0x74
        opcodes[0x75] = load8.op0x75
        // opcodes[0x76] = load8.op0x76 this is halt instruction
        opcodes[0x77] = load8.op0x77
        opcodes[0x78] = load8.op0x78
        opcodes[0x79] = load8.op0x79
        opcodes[0x7a] = load8.op0x7a
        opcodes[0x7b] = load8.op0x7b
        opcodes[0x7c] = load8.op0x7c
        opcodes[0x7d] = load8.op0x7d
        opcodes[0x7e] = load8.op0x7e
        opcodes[0x7f] = load8.op0x7f
        opcodes[0x02] = load8.op0x02
        opcodes[0x12] = load8.op0x12
        opcodes[0x22] = load8.op0x22
        opcodes[0x32] = load8.op0x32
        opcodes[0x06] = load8.op0x06
        opcodes[0x16] = load8.op0x16
        opcodes[0x26] = load8.op0x26
        opcodes[0x36] = load8.op0x36
        opcodes[0x0a] = load8.op0x0a
        opcodes[0x1a] = load8.op0x1a
        opcodes[0x2a] = load8.op0x2a
        opcodes[0x3a] = load8.op0x3a
        opcodes[0x0e] = load8.op0x0e
        opcodes[0x1e] = load8.op0x1e
        opcodes[0x2e] = load8.op0x2e
        opcodes[0x3e] = load8.op0x3e
        opcodes[0xe0] = load8.op0xe0
        opcodes[0xf0] = load8.op0xf0
        opcodes[0xe2] = load8.op0xe2
        opcodes[0xf2] = load8.op0xf2
        opcodes[0xea] = load8.op0xea
        opcodes[0xfa] = load8.op0xfa

        opcodes[0x80] = arithmetic8.op0x80
        opcodes[0x81] = arithmetic8.op0x81
        opcodes[0x82] = arithmetic8.op0x82
        opcodes[0x83] = arithmetic8.op0x83
        opcodes[0x84] = arithmetic8.op0x84
        opcodes[0x85] = arithmetic8.op0x85
        opcodes[0x86] = arithmetic8.op0x86
        opcodes[0x87] = arithmetic8.op0x87
        opcodes[0x88] = arithmetic8.op0x88
        opcodes[0x89] = arithmetic8.op0x89
        opcodes[0x8a] = arithmetic8.op0x8a
        opcodes[0x8b] = arithmetic8.op0x8b
        opcodes[0x8c] = arithmetic8.op0x8c
        opcodes[0x8d] = arithmetic8.op0x8d
        opcodes[0x8e] = arithmetic8.op0x8e
        opcodes[0x8f] = arithmetic8.op0x8f
        opcodes[0x90] = arithmetic8.op0x90
        opcodes[0x91] = arithmetic8.op0x91
        opcodes[0x92] = arithmetic8.op0x92
        opcodes[0x93] = arithmetic8.op0x93
        opcodes[0x94] = arithmetic8.op0x94
        opcodes[0x95] = arithmetic8.op0x95
        opcodes[0x96] = arithmetic8.op0x96
        opcodes[0x97] = arithmetic8.op0x97
        opcodes[0x98] = arithmetic8.op0x98
        opcodes[0x99] = arithmetic8.op0x99
        opcodes[0x9a] = arithmetic8.op0x9a
        opcodes[0x9b] = arithmetic8.op0x9b
        opcodes[0x9c] = arithmetic8.op0x9c
        opcodes[0x9d] = arithmetic8.op0x9d
        opcodes[0x9e] = arithmetic8.op0x9e
        opcodes[0x9f] = arithmetic8.op0x9f
        opcodes[0xa0] = arithmetic8.op0xa0
        opcodes[0xa1] = arithmetic8.op0xa1
        opcodes[0xa2] = arithmetic8.op0xa2
        opcodes[0xa3] = arithmetic8.op0xa3
        opcodes[0xa4] = arithmetic8.op0xa4
        opcodes[0xa5] = arithmetic8.op0xa5
        opcodes[0xa6] = arithmetic8.op0xa6
        opcodes[0xa7] = arithmetic8.op0xa7
        opcodes[0xa8] = arithmetic8.op0xa8
        opcodes[0xa9] = arithmetic8.op0xa9
        opcodes[0xaa] = arithmetic8.op0xaa
        opcodes[0xab] = arithmetic8.op0xab
        opcodes[0xac] = arithmetic8.op0xac
        opcodes[0xad] = arithmetic8.op0xad
        opcodes[0xae] = arithmetic8.op0xae
        opcodes[0xaf] = arithmetic8.op0xaf
        opcodes[0xb0] = arithmetic8.op0xb0
        opcodes[0xb1] = arithmetic8.op0xb1
        opcodes[0xb2] = arithmetic8.op0xb2
        opcodes[0xb3] = arithmetic8.op0xb3
        opcodes[0xb4] = arithmetic8.op0xb4
        opcodes[0xb5] = arithmetic8.op0xb5
        opcodes[0xb6] = arithmetic8.op0xb6
        opcodes[0xb7] = arithmetic8.op0xb7
        opcodes[0xb8] = arithmetic8.op0xb8
        opcodes[0xb9] = arithmetic8.op0xb9
        opcodes[0xba] = arithmetic8.op0xba
        opcodes[0xbb] = arithmetic8.op0xbb
        opcodes[0xbc] = arithmetic8.op0xbc
        opcodes[0xbd] = arithmetic8.op0xbd
        opcodes[0xbe] = arithmetic8.op0xbe
        opcodes[0xbf] = arithmetic8.op0xbf
        opcodes[0x04] = arithmetic8.op0x04
        opcodes[0x14] = arithmetic8.op0x14
        opcodes[0x24] = arithmetic8.op0x24
        opcodes[0x34] = arithmetic8.op0x34
        opcodes[0x05] = arithmetic8.op0x05
        opcodes[0x15] = arithmetic8.op0x15
        opcodes[0x25] = arithmetic8.op0x25
        opcodes[0x35] = arithmetic8.op0x35
        opcodes[0x0c] = arithmetic8.op0x0c
        opcodes[0x1c] = arithmetic8.op0x1c
        opcodes[0x2c] = arithmetic8.op0x2c
        opcodes[0x3c] = arithmetic8.op0x3c
        opcodes[0x0d] = arithmetic8.op0x0d
        opcodes[0x1d] = arithmetic8.op0x1d
        opcodes[0x2d] = arithmetic8.op0x2d
        opcodes[0x3d] = arithmetic8.op0x3d
        opcodes[0xc6] = arithmetic8.op0xc6
        opcodes[0xd6] = arithmetic8.op0xd6
        opcodes[0xe6] = arithmetic8.op0xe6
        opcodes[0xf6] = arithmetic8.op0xf6
        opcodes[0xce] = arithmetic8.op0xce
        opcodes[0xde] = arithmetic8.op0xde
        opcodes[0xee] = arithmetic8.op0xee
        opcodes[0xfe] = arithmetic8.op0xfe

        opcodes[0x03] = arithmetic16.op0x03
        opcodes[0x09] = arithmetic16.op0x09
        opcodes[0x0b] = arithmetic16.op0x0b
        opcodes[0x13] = arithmetic16.op0x13
        opcodes[0x19] = arithmetic16.op0x19
        opcodes[0x1b] = arithmetic16.op0x1b
        opcodes[0x23] = arithmetic16.op0x23
        opcodes[0x29] = arithmetic16.op0x29
        opcodes[0x2b] = arithmetic16.op0x2b
        opcodes[0x33] = arithmetic16.op0x33
        opcodes[0x39] = arithmetic16.op0x39
        opcodes[0x3b] = arithmetic16.op0x3b
        opcodes[0xe8] = arithmetic16.op0xe8

        opcodes[0x01] = load16.op0x01
        opcodes[0x08] = load16.op0x08
        opcodes[0x11] = load16.op0x11
        opcodes[0x21] = load16.op0x21
        opcodes[0x31] = load16.op0x31
        opcodes[0xc1] = load16.op0xc1
        opcodes[0xc5] = load16.op0xc5
        opcodes[0xd1] = load16.op0xd1
        opcodes[0xd5] = load16.op0xd5
        opcodes[0xe1] = load16.op0xe1
        opcodes[0xe5] = load16.op0xe5
        opcodes[0xf1] = load16.op0xf1
        opcodes[0xf5] = load16.op0xf5
        opcodes[0xf8] = load16.op0xf8
        opcodes[0xf9] = load16.op0xf9

        opcodes[0x07] = rotation.op0x07
        opcodes[0x0f] = rotation.op0x0f
        opcodes[0x17] = rotation.op0x17
        opcodes[0x1f] = rotation.op0x1f
        opcodes[0xcb] = rotation.op0xcb
    }

    fun readNextByte(): Int {   // reads the memory specified by program counter; increment program counter
        registers.programCounter += 1
        return memoryMap.getValue(registers.programCounter -1)
    }

    override fun toString(): String {
        return registers.toString() +
                "Machine Cycles: $machineCycles" +
                "\nHALT: $haltMode\nSTOP: $stopMode\nDOUBLE SPEED: $doubleSpeedMode\nIME: $ime\n"
    }
}