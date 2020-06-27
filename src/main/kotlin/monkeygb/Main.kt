// Main.kt
// Version 1.1

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
