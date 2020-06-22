// MemoryMap.kt
// Version 1.0
// Implements the GameBoy memory mapping

package monkeygb.memory

class MemoryMap {
    // TODO: Implement bank switching
    private val gameRom = Memory(0x8000, 0)
    private val vRam = Memory(0x2000, 0x8000)
    private val workRam = Memory(0x2000, 0xc000)
    private val oam = Memory(0x9f, 0xfe00)
    private val ioRegisters = Memory(0x7f, 0xff00)
    private val highRam = Memory(0x7e, 0xff80)
    private val interruptEnableRegister = Memory(1, 0xffff)

    fun getValue(address: Int): Int = when {
        gameRom.validAddress(address) -> gameRom.getValue(address)
        vRam.validAddress(address) -> vRam.getValue(address)
        workRam.validAddress(address) -> workRam.getValue(address)
        oam.validAddress(address) -> oam.getValue(address)
        ioRegisters.validAddress(address) ->ioRegisters.getValue(address)
        highRam.validAddress(address) -> highRam.getValue(address)
        interruptEnableRegister.validAddress(address) -> interruptEnableRegister.getValue(address)
        else -> -1
    }

    fun setValue(address: Int, value: Int) {
        when {
            gameRom.validAddress(address) -> gameRom.setValue(address, value)
            vRam.validAddress(address) -> vRam.setValue(address, value)
            workRam.validAddress(address) -> workRam.setValue(address, value)
            oam.validAddress(address) -> oam.setValue(address, value)
            ioRegisters.validAddress(address) ->ioRegisters.setValue(address, value)
            highRam.validAddress(address) -> highRam.setValue(address, value)
            interruptEnableRegister.validAddress(address) -> interruptEnableRegister.setValue(address, value)
        }
    }
}