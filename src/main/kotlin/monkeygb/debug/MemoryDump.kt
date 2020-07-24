// MemoryDump.kt
// Version 1.0
// Implements memory dump to check current Game Boy memory state

package monkeygb.debug

import monkeygb.memory.MemoryMap

class MemoryDump(private val memoryMap: MemoryMap) {
    // Returns a string containing values from memory address from to memory address to
    fun dump(from: Int, to: Int): String {
        var j = 0
        var ret: String = ""
        for (i in from until to) {
            var iString = i.toString(16)
            var valueString = memoryMap.getValue(i).toString(16)

            // iString must be 4 digits long, valueString must be 2 digits long
            while (iString.length < 4)
                iString = "0$iString"
            while (valueString.length < 2)
                valueString = "0$valueString"

            if (j++ == 0)
                ret += "$iString: "

            ret += "$valueString "

            if (j == 8) {
                j = 0
                ret += "\n"
            }
        }
        return ret + "\n"
    }
}