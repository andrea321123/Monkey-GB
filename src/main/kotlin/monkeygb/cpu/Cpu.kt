// Cpu.kt
// Version 1.1
// Emulates the GameBoy CPU (SHARP LR35902)

package monkeygb.cpu

import monkeygb.memory.MemoryMap
import monkeygb.registers.Registers

class Cpu {
    val memoryMap = MemoryMap()
    val registers = Registers()

    var machineCycles: Long = 0     // number of machine cycles executed; frequency: 1 MHz

    // cpu modes
    var haltMode: Boolean = false
    var stopMode: Boolean = false
    var doubleSpeedMode: Boolean = false

    // instructions implementations are in their own category-specific class
    val control =  ControlInstructions(this)
    val jump = JumpCallsInstructions(this)
    val load8 = LoadInstructions8bit(this)
    val load16 = LoadInstructions16bit(this)
    val arithmetic8 = ArithmeticLogical8bit(this)
    val arithmetic16 = ArithmeticLogical16bit(this)
    val rotation = RotationShiftBitInstructions(this)

    val opcodes = mutableMapOf<Int, () -> Unit>()   // key: instruction opcode, value: instruction implementation
}