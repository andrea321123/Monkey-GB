// Main.kt
// Version 1.10

package monkeygb

import monkeygb.cartridge.Cartridge
import monkeygb.cpu.Cpu
import monkeygb.debug.MemoryDump
import monkeygb.debug.render.DebugRenderTiles
import monkeygb.interrupts.InterruptHandler
import monkeygb.joypad.Joypad
import monkeygb.ppu.Lcd
import monkeygb.ppu.Ppu
import monkeygb.ppu.renderer.Renderer
import monkeygb.timer.Timer


val cpu = Cpu()
val memoryMap = cpu.memoryMap
val interruptHandler = InterruptHandler(cpu)
val ppu = Ppu(memoryMap)
val lcd = Lcd(memoryMap, interruptHandler, ppu)
val joypad = Joypad(memoryMap, interruptHandler)
val renderer = Renderer(joypad)
val timer = Timer(memoryMap, interruptHandler)
val dump = MemoryDump(memoryMap)
lateinit var debugTiles: DebugRenderTiles

const val MAX_CYCLES = 69905

fun main(args: Array<String>) {
    val cartridge = Cartridge(args[0], memoryMap)
    memoryMap.cartridge = cartridge

    val DEBUG = false
    if(DEBUG)
        debugTiles = DebugRenderTiles(memoryMap)

    cpu.afterBootRom()

    while (true) {
        var cycleThisUpdate: Long = 0
        val startTime: Long = System.nanoTime() / 1000

        while (cycleThisUpdate < MAX_CYCLES) {
            var machineCycles = cpu.machineCycles
            cpu.executeInstruction()
            //File("log.txt").appendText(cpu.registers.programCounter.toString() + "\n")

            var lastInstructionCycles: Long = (cpu.machineCycles - machineCycles) *4
            cycleThisUpdate += lastInstructionCycles
            lcd.updateGraphics(lastInstructionCycles)
            timer.updateTimers(lastInstructionCycles.toInt())
            interruptHandler.checkInterrupts()
            //println(cpu.registers.programCounter)
        }

        if (DEBUG) {
            debugTiles.update()
            println(dump.dump(0x9800, 0x9c00))
        }

        renderer.renderDisplay(ppu.renderWindow)

        // this loop should be executed 60 times per second
        while (((System.nanoTime() /1000) - startTime) < 16666) {
            val wasteTime = 0
        }
    }
}

// returns the int value of a complement's 2 number
fun complement2toInt(e: Int): Int {
    val negativeE = -(e and 0b10000000)
    val positiveE = e and 0b01111111
    return positiveE + negativeE
}

// returns bit n of a given number in boolean type (LSB is bit 0)
fun getBit(number: Int, bit: Int): Boolean {
    return (number shr bit) and 0x1 == 0x1
}

// returns a new number that differs from number (param) only for bit n (param) set to set (param)
fun setBit(number: Int, bit: Int, set: Boolean): Int {
    var newNumber: Int = number
    var offset: Int = 1

    offset = offset shl bit

    if (set)
        newNumber = newNumber or offset
    else {
        offset = offset.inv()
        newNumber = newNumber and offset
    }

    return newNumber
}

