// Main.kt
// Version 1.2

package monkeygb

fun main(args: Array<String>) {
    println("Hello, World")
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