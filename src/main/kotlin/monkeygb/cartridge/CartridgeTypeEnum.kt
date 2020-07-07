// CartridgeTypeEnum.kt
// Version 1.0
// Enum of possible cartridge types (because of eventual Memory Bank Controller for bank switching)

package monkeygb.cartridge

enum class CartridgeTypeEnum {
    NO_MBC,
    MBC1,
    MBC2,
    MBC3,
    MBC4,
    MBC5,
    MBC6,
    MBC7,
    OTHER
}