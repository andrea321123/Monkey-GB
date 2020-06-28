// ControlInstructionsTest.kt
// Version 1.0

package monkeygb.cpu

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ControlInstructionsTest {
    @Test
    fun daaTest() {
        val cpu = Cpu()
        val test = ControlInstructions(cpu)
        val math = ArithmeticLogical8bit(cpu)

        cpu.registers.a = 0x45
        cpu.registers.b = 0x38
        math.op0x80.invoke()

        test.op0x27.invoke()
        assertEquals(cpu.registers.a, 0x83)
        assertFalse(cpu.registers.carryFlag)

        math.op0x90.invoke()
        test.op0x27.invoke()
        assertEquals(cpu.registers.a, 0x45)
    }
}