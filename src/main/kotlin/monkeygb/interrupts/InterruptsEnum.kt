// InterruptsEnum.kt
// Version 1.0
// Enum of possible hardware interrupts

package monkeygb.interrupts

enum class InterruptsEnum {
    V_BLANK_INTERRUPT,
    LCD_INTERRUPT,
    TIMER_INTERRUPT,
    JOYPAD_INTERRUPT,
    SERIAL_INTERRUPT
}