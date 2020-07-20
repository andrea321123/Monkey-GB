// MemoryMap.kt
// Version 1.10
// Implements the GameBoy memory mapping

package monkeygb.memory

import monkeygb.cartridge.Cartridge
import monkeygb.cartridge.CartridgeTypeEnum
import monkeygb.getBit
import monkeygb.memoryMap
import monkeygb.ppu.HORIZONTAL_LINES

// I/O registers
const val INTERRUPT_FLAG = 0xff0f
const val INTERRUPT_ENABLE = 0xffff
const val LCDC = 0xff40
const val STAT = 0xff41
const val LY = 0xff44       // vertical line to which the actual data is transferred to the LCD Driver
const val LYC = 0xff45      // vertical line compare
const val DMA = 0xff46
const val SCROLL_Y = 0xff42
const val SCROLL_X = 0xff43
const val WY = 0xff4a       // window y position
const val WX = 0xff4b       // window x position - 7
const val BGP = 0xff47      // background palette data
const val JOYP = 0xff00  // joypad

// timer registers
const val DIV = 0xff04      // divider register
const val TIMA = 0xff05     // timer counter
const val TMA = 0xff06      // timer modulo
const val TAC = 0xff07      // timer control

// for memory banking
private var currentRomBank: Int = 1
private var currentRamBank: Int = 0
private var ramBankEnabled: Boolean = false     // by default you can't write to Ram bank (it must be enabled)
private var romBanking: Boolean = false

class MemoryMap {
    // TODO: Implement bank switching
    private val bank0Rom = Memory(0x4000, 0)
    private val secondaryBankRom = Memory(0x4000, 0x4000)
    private val vRam = Memory(0x2000, 0x8000)
    private val externalRam = Memory(0x2000, 0xa000)
    private val workRam = Memory(0x2000, 0xc000)
    private val oam = Memory(0x9f, 0xfe00)
    private val ioRegisters = Memory(0x7f, 0xff00)
    private val highRam = Memory(0x7f, 0xff80)
    private val interruptEnableRegister = Memory(1, 0xffff)

    lateinit var cartridge: Cartridge
    private val dma = Dma(this)

    var directionalNibble = 0b1111
    var buttonNibble = 0b1111

    fun getValue(address: Int): Int {
        if (address == JOYP)
            return getRightJoypadInput()

         return when {
             bank0Rom.validAddress(address) -> bank0Rom.getValue(address)
             secondaryBankRom.validAddress(address) -> secondaryBankRom.getValue(address)
             vRam.validAddress(address) -> vRam.getValue(address)
            workRam.validAddress(address) -> workRam.getValue(address)
            oam.validAddress(address) -> oam.getValue(address)
            ioRegisters.validAddress(address) -> ioRegisters.getValue(address)
            highRam.validAddress(address) -> highRam.getValue(address)
            interruptEnableRegister.validAddress(address) -> interruptEnableRegister.getValue(address)
            else -> -1
        }
    }

    fun setValue(address: Int, value: Int) {
        // writing to handle memory banking
        if (address < 0x8000) {    // if program writes to ROM, we have to handle memory banking
            handleBanking(address, value)
        }

        // writing to special memory address (I/O registers)
        // if CPU tries to write to LY the content of LY resets
        if (address == LY) {
            ioRegisters.setValue(0xff44, 0)
            return
        }
        // if CPU write to DMA, start a DMA transfer
        else if (address == DMA)
            dma.dmaTransfer(memoryMap.getValue(address))
        // if CPU writes to JOYP, we set the memory content the appropriate way
        else if (address == JOYP) {
            var newValue = value or 0b11000000
            newValue = newValue.inv()
            newValue = value or 0b11000000
            ioRegisters.setValue(JOYP, newValue)
            return
        }
        else if (address == DIV) {
            ioRegisters.setValue(DIV, 0)
            return
        }

        // normal memory writing
        when {
            bank0Rom.validAddress(address) -> bank0Rom.setValue(address, value)
            secondaryBankRom.validAddress(address) -> secondaryBankRom.setValue(address, value)
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

    // since getValue(JOYP) will return joypad state
    private fun getJoyp() = ioRegisters.getValue(JOYP)

    private fun getRightJoypadInput(): Int {
        if (!getBit(getJoyp(),4))       // looking for directional keys
            return getJoyp() or directionalNibble
        else if (!getBit(getJoyp(), 5))     // looking for button keys
            return getJoyp() or buttonNibble
        return 0
    }

    // since DIV cannot be incremented from normal load instructions
    fun incrementDiv() {
        if (getValue(DIV) == 255)
            ioRegisters.setValue(DIV, 0)
        else
            ioRegisters.setValue(DIV, getValue(DIV) +1)
    }

    private fun handleBanking(address: Int, value: Int) {
        // RAM enabling
        if (address < 0x2000) {
            if (cartridge.ram)
                enableRamBank(address, value)
        }

        // ROM bank changing
        else if (cartridge.cartridgeType != CartridgeTypeEnum.NO_MBC) {
            if ((address >= 0x2000) && (address < 0x4000))
                changeLowRomBank(value)
        }

        // ROM or RAM bank changing
        else if ((address >= 0x4000) && (address < 0x6000)) {
            if (romBanking)
                changeHighRomBank(value)
            else
                changeRamBank(value)
        }

        // change if we are doing ROM or RAM banking
        if (address <= 0x6000 && address < 0x8000)
            if (cartridge.cartridgeType != CartridgeTypeEnum.NO_MBC)
                changeRomRamMode(value)
    }

    // returns string containing IO registers
    fun getIORegisters(): String {
        return "LCDC [0xff40]: ${memoryMap.getValue(LCDC)}\n" +
                "STAT [0xff41]: ${memoryMap.getValue(STAT)}\n" +
                "SCROLL Y [0xff42]: ${memoryMap.getValue(SCROLL_Y)}\n" +
                "SCROLL X [0xff43]: ${memoryMap.getValue(SCROLL_X)}\n" +
                "LY [0xff44]: ${memoryMap.getValue(LY)}\n" +
                "LYC [0xff45]: ${memoryMap.getValue(LYC)}\n" +
                "BGP [0xff47]: ${memoryMap.getValue(BGP)}\n" +
                "WY [0xff4a]: ${memoryMap.getValue(WY)}\n" +
                "WX [0xff4b]: ${memoryMap.getValue(WX)}\n"
    }
}