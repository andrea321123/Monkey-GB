// MemoryMapTest.kt
// Version 1.0

package monkeygb.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MemoryMapTest {
    @Test
    fun getSetValueTest() {
        val test = MemoryMap()
        assertEquals(test.getValue(0xfea1), -1)
        test.setValue(0x3ffe, 15)
        assertEquals(test.getValue(0x3ffe), 15)
        test.setValue(0xfea2, 10)       // writing to non usable memory
        assertEquals(test.getValue(0xfea2), -1)
    }
}