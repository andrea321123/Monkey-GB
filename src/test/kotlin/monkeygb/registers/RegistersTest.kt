// RegistersTest.kt
// Version 1.0

package monkeygb.registers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RegistersTest {
    @Test
    fun getSetBCTest() {
        val test = Registers()
        test.b = 0x15
        test.c = 0x3a
        assertEquals(test.getBC(), 5434)

        test.setBC(9530)
        assertEquals(test.b, 0x25)
        assertEquals(test.c, 0x3a)
    }
    @Test
    fun getSetDETest() {
        val test = Registers()
        test.d = 69
        test.e = 12
        assertEquals(test.getDE(), 0x450c)

        test.setDE(0xaaaa)
        assertEquals(test.d, 170)
        assertEquals(test.e, 170)
    }
    @Test
    fun getSetHLTest() {
        val test = Registers()
        test.h = 0x15
        test.l = 0x3a
        assertEquals(test.getHL(), 5434)

        test.setHL(9530)
        assertEquals(test.h, 0x25)
        assertEquals(test.l, 0x3a)
    }
}