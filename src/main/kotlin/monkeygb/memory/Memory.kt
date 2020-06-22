// Memory.kt
// Version 1.0
// Implementation of a general memory bank

package monkeygb.memory

class Memory(private val size: Int, private val offset: Int){
    private val memoryArray = IntArray(size)

    fun validAddress(address: Int): Boolean = address >= offset && address < offset + size

    fun getValue(address: Int): Int = memoryArray[address - offset]

    fun setValue(address: Int, value: Int) {
        memoryArray[address - offset] = value
    }
}