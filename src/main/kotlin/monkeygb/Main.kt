// Main.kt
// Version 1.4

package monkeygb

import monkeygb.cartridge.Cartridge
import monkeygb.cpu.Cpu
import monkeygb.interrupts.InterruptHandler
import monkeygb.ppu.Lcd
import monkeygb.ppu.Ppu
import monkeygb.ppu.renderer.Renderer
import java.io.File


val cpu = Cpu()
val memoryMap = cpu.memoryMap
val interruptHandler = InterruptHandler(cpu)
val ppu = Ppu(memoryMap)
val lcd = Lcd(memoryMap, interruptHandler, ppu)
val renderer = Renderer()
val cartridge = Cartridge("Tetris.gb", memoryMap)

const val MAX_CYCLES = 69905

fun main(args: Array<String>) {
    cpu.afterBootRom()
    //File("log.txt").writeText("Program counter: \n")

    while (true) {
        var cycleThisUpdate: Long = 0
        while (cycleThisUpdate < MAX_CYCLES) {
            var machineCycles = cpu.machineCycles
            cpu.executeInstruction()
            //File("log.txt").appendText(cpu.registers.programCounter.toString() + "\n")

            var lastInstructionCycles: Long = (cpu.machineCycles - machineCycles) *4
            cycleThisUpdate += lastInstructionCycles
            lcd.updateGraphics(lastInstructionCycles)
            interruptHandler.checkInterrupts()
        }
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

