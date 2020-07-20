// Main.kt
// Version 1.7

package monkeygb

import monkeygb.cartridge.Cartridge
import monkeygb.cpu.Cpu
import monkeygb.interrupts.InterruptHandler
import monkeygb.joypad.Joypad
import monkeygb.memory.DIV
import monkeygb.memory.TIMA
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
val cartridge = Cartridge("roms/Bubble Ghost.gb", memoryMap)
val timer = Timer(memoryMap, interruptHandler)

const val MAX_CYCLES = 69905

fun main(args: Array<String>) {
    // initialization
    cpu.afterBootRom()
    memoryMap.cartridge = cartridge

    //File("log.txt").writeText("Program counter: \n")

    while (true) {
        var cycleThisUpdate: Long = 0
        val startTime: Long = System.nanoTime()
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

        //waste time
       /* while (((System.nanoTime() - startTime) /1000) < 16666) {
            val wasteTime = 9
        }*/
        renderer.renderDisplay(ppu.renderWindow)
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

