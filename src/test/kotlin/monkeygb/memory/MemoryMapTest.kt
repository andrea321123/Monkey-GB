// MemoryMapTest.kt
// Version 1.1

package monkeygb.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class MemoryMapTest {
    @Test
    fun getSetValueTest() {
        val test = MemoryMap()
        assertEquals(test.getValue(0xfea1), -1)
        test.setValue(0xfffe, 15)
        assertEquals(test.getValue(0xfffe), 15)
        test.setValue(0xfea2, 10)       // writing to non usable memory
        assertEquals(test.getValue(0xfea2), -1)
    }
}