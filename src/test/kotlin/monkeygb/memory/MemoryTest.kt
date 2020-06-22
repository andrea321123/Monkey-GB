// MemoryTest.kt
// Version 1.0

package monkeygb.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MemoryTest {
    @Test
    fun validAddressTest() {
        val test = Memory(1024, 256)
        assertTrue(test.validAddress(256))
        assertTrue(test.validAddress(512))
        assertTrue(test.validAddress(1279))

        assertFalse(test.validAddress(1280))
        assertFalse(test.validAddress(0))
        assertFalse(test.validAddress(100000))
    }

    @Test
    fun getSetValueTest() {
        val test = Memory(512, 2048)
        assertEquals(test.getValue(2048), 0)
        test.setValue(2500, 35)
        assertEquals(test.getValue(2500), 35)
    }
}