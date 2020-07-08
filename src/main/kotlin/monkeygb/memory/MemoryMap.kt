// MemoryMap.kt
// Version 1.6
// Implements the GameBoy memory mapping

package monkeygb.memory

import monkeygb.ppu.HORIZONTAL_LINES

// I/O registers
const val INTERRUPT_FLAG = 0xff0f
const val INTERRUPT_ENABLE = 0xffff
const val LCDC = 0xff40
const val STAT = 0xff41
const val LY = 0xff44       // vertical line to which the present data is transferred to the LCD Driver
const val LYC = 0xff45      // vertical line compare
const val DMA = 0xff46

class MemoryMap {
    // TODO: Implement bank switching
    private val gameRom = Memory(0x8000, 0)
    private val vRam = Memory(0x2000, 0x8000)
    private val workRam = Memory(0x2000, 0xc000)
    private val oam = Memory(0x9f, 0xfe00)
    private val ioRegisters = Memory(0x7f, 0xff00)
    private val highRam = Memory(0x7f, 0xff80)
    private val interruptEnableRegister = Memory(1, 0xffff)

    private val dma = Dma(this)

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
        // if CPU tries to write to LY the content of LY resets
        if (address == LY) {
            ioRegisters.setValue(0xff44, 0)
            return
        }
        // if CPU write to DMA, start a DMA transfer
        else if (address == DMA)
            dma.dmaTransfer(address)

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

    // since LY cannot be incremented from normal load instructions
    fun incrementLY() {
        if (getValue(LY) == HORIZONTAL_LINES -1)
            ioRegisters.setValue(0xff44, 0)
        else
            ioRegisters.setValue(0xff44, getValue(LY) +1)
    }
}