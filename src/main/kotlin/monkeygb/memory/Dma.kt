// Dma.kt
// Version 1.0
// Implements Direct Memory Access

package monkeygb.memory

class Dma(private val memoryMap: MemoryMap) {
    fun dmaTransfer(data: Int) {
        val sourceAddress = data shl 8      // source address is data * 0x100
        for (i in 0 until 0xa0)
            memoryMap.setValue(0xfe00+ i, memoryMap.getValue(sourceAddress + i))
    }
}