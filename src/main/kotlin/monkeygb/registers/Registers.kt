// Registers.kt
// Version 1.1
// Implements all GameBoy registers

package monkeygb.registers

class Registers {
    var a: Int = 0
    var b: Int = 0
    var c: Int = 0
    var d: Int = 0
    var e: Int = 0
    var h: Int = 0
    var l: Int = 0
    var programCounter: Int = 0
    var stackPointer: Int = 0

    //flag register
    var zeroFlag: Boolean = false
    var addSubFlag: Boolean = false
    var halfCarryFlag: Boolean = false
    var carryFlag: Boolean = false

    fun getBC(): Int = (b shl 8) + c
    fun getDE(): Int = (d shl 8) + e
    fun getHL(): Int = (h shl 8) + l

    fun setBC(value: Int) {
        b = value shr 8
        c = value and 0xff
    }
    fun setDE(value: Int) {
        d = value shr 8
        e = value and 0xff
    }
    fun setHL(value: Int) {
        h = value shr 8
        l = value and 0xff
    }

    override fun toString(): String {
        return "A: ${a.toString(16)}\n" +
                "B: ${b.toString(16)}    C: ${b.toString(16)}\n" +
                "D: ${d.toString(16)}    E: ${e.toString(16)}\n" +
                "H: ${h.toString(16)}    L: ${h.toString(16)}\n" +
                "SP: ${stackPointer.toString(16)}\nPC: ${programCounter.toString(16)}" +
                "\nZF: ${zeroFlag.toString()}\nNF: ${addSubFlag.toString()}"  +
                "\nHF: ${halfCarryFlag.toString()}\nCF: ${carryFlag.toString()}\n"
    }
}