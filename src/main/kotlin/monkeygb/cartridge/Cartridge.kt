// Cartridge.kt
// Version 1.2
// Implements the Game Boy cartridge

package monkeygb.cartridge

import monkeygb.memory.MemoryMap
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.experimental.and


class Cartridge(private val romName: String, private val memoryMap: MemoryMap) {
    val byteArray: ByteArray
    val gameArray: IntArray
    var cartridgeType: CartridgeTypeEnum = CartridgeTypeEnum.NO_MBC
    var battery = false
    var ram = false
    var timer = false

    init {
        // loading game from ROM file
        var file: File = File(romName)
        var inputStream: InputStream = FileInputStream(file)
        byteArray = inputStream.readAllBytes()
        gameArray = IntArray(byteArray.size)
        for (i in byteArray.indices) {
            gameArray[i] = (byteArray[i].toUByte() and 0xff.toUByte()).toUInt().toInt()
        }

        // assign cartridgeType by reading byte 0x147
        when (gameArray[0x147]) {
            0x0 -> cartridgeType = CartridgeTypeEnum.NO_MBC
            0x1 -> cartridgeType = CartridgeTypeEnum.MBC1
            0x2 -> {
                cartridgeType = CartridgeTypeEnum.MBC1
                ram = true
            }
            0x3 -> {
                cartridgeType = CartridgeTypeEnum.MBC1
                ram = true
                battery = true
            }
            0x5 -> cartridgeType = CartridgeTypeEnum.MBC2
            0x6 -> {
                cartridgeType = CartridgeTypeEnum.MBC2
                battery = true
            }
            0x8 -> {
                cartridgeType = CartridgeTypeEnum.NO_MBC
                ram = true
            }
            0x9 -> {
                cartridgeType = CartridgeTypeEnum.NO_MBC
                ram = true
                battery = true
            }
            0xb -> cartridgeType = CartridgeTypeEnum.OTHER
            0xc -> cartridgeType = CartridgeTypeEnum.OTHER
            0xd -> cartridgeType = CartridgeTypeEnum.OTHER
            0xf -> {
                cartridgeType = CartridgeTypeEnum.MBC3
                timer = true
                battery = true
            }
            0x10 -> {
                cartridgeType = CartridgeTypeEnum.MBC3
                timer = true
                battery = true
                ram = true
            }
            0x11 -> cartridgeType = CartridgeTypeEnum.MBC3
            0x12 -> {
                cartridgeType = CartridgeTypeEnum.MBC3
                ram = true
            }
            0x13 -> {
                cartridgeType = CartridgeTypeEnum.MBC3
                ram = true
                battery = true
            }
            0x1a -> {
                cartridgeType = CartridgeTypeEnum.MBC5
                ram = true
            }
            0x19 -> cartridgeType = CartridgeTypeEnum.MBC5
            0x1b -> {
                cartridgeType = CartridgeTypeEnum.MBC5
                ram = true
                battery = true
            }
            0x1c -> cartridgeType = CartridgeTypeEnum.MBC5
            0x1d -> {
                cartridgeType = CartridgeTypeEnum.MBC5
                ram = true
            }
            0x1e -> {
                cartridgeType = CartridgeTypeEnum.MBC5
                ram = true
                battery = true
            }
            0x20 -> cartridgeType = CartridgeTypeEnum.MBC6
            0x22 -> {
                cartridgeType = CartridgeTypeEnum.MBC7
                ram = true
                battery = true
            }
            else -> cartridgeType = CartridgeTypeEnum.OTHER
        }

    }

}