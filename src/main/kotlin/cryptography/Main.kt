package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.pow

/**
 * @author Mack_TB
 * @version 1.0.2
 * @since 10/11/2021
 */

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        when(val command = readLine()) {
            "hide" -> hide()
            "show" -> show()
            "exit" -> {
                println("Bye!")
                break
            }
            else -> println("Wrong task: $command")
        }
    }
}

fun hide() {
    println("Input image file:")
    val infileName = readLine()!!.toString()
    val inImageFile = File(infileName)
    if (!inImageFile.exists()) {
        println("Can't read input file!")
    }
    println("Output image file:")
    val outfileName = readLine()!!.toString()
    val outImageFile = File(outfileName)
    println("Message to hide:")
    var secretMessage = readLine()!!.encodeToByteArray()
    secretMessage = add3Bytes(secretMessage)
    val inputImage : BufferedImage = ImageIO.read(inImageFile)
    if (secretMessage.size > inputImage.width + inputImage.height) {
        println("The input image is not large enough to hold this message.")
        return
    }
    insert(secretMessage, inputImage)
    ImageIO.write(inputImage, "png", outImageFile)
    println("Message saved in $outfileName image.")
}

fun show() {
    println("Input image file:")
    val infileName = readLine()!!.toString()
    val inImageFile = File(infileName)
    val inputImage: BufferedImage = ImageIO.read(inImageFile)
    val secretMessageBA = mutableListOf<Byte>()
    var byte = ""
    var index: Int
    loop@ for (y in 0 until inputImage.height) {
        for (x in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(x, y))
            val bit = getBit(color.blue, 0)
            byte = byte.plus(bit)
            if (byte.length == 8) {
                secretMessageBA.add(convertBinaryStringToDec(byte))
                byte = ""
                index = secretMessageBA.size
                if (secretMessageBA.size >= 3 && secretMessageBA[index - 1] == 3.toByte() &&
                    secretMessageBA[index - 2] == 0.toByte() && secretMessageBA[index - 3] == 0.toByte()) {
                    break@loop
                }
            }
        }
    }
    index = secretMessageBA.size
    secretMessageBA.removeAt(--index)
    secretMessageBA.removeAt(--index)
    secretMessageBA.removeAt(--index)
    val message = secretMessageBA.toByteArray().toString(Charsets.UTF_8)
    println("Message:\n$message")
}

fun insert(secretMessage: ByteArray, inputImage: BufferedImage) {
    var position = 7
    var m = 0
    loop@ for (y in 0 until inputImage.height) {
        for (x in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(x, y))
            val b = putLastBitTo(color.blue, getBit(secretMessage[m].toInt(), position))
            val rgb = Color(
                color.red,
                color.green,
                b
            ).rgb
            inputImage.setRGB(x, y, rgb)
            position--
            if (position == -1) {
                position = 7
                m++
            }
            if (m == secretMessage.size) break@loop
        }
    }
}

fun getBit(value: Int, position: Int): Int {
    return (value shr position) and 1
}

fun putLastBitTo(blue: Int, b: Int): Int {
    return if (b == 0) {
        blue and 254
    } else {
        blue or b
    }
}

fun add3Bytes(secretMessage: ByteArray): ByteArray {
    val message = ByteArray(secretMessage.size + 3)
    for (i in secretMessage.indices) {
        message[i] = secretMessage[i]
    }
    var indice = secretMessage.size
    message[indice] = 0
    message[++indice] = 0
    message[++indice] = 3
    return message
}

fun convertBinaryStringToDec(binaryString: String): Byte {
    var result = 0
    var power = 0
    for (i in binaryString.length - 1 downTo 0) {
        result += binaryString[i].toString().toInt() * 2.0.pow(power.toDouble()).toInt()
        power++
    }
    return result.toByte()
}

fun setLeastSignificantBitToOne(pixel: Int) : Int {
    return if (pixel % 2 == 0) pixel + 1
    else pixel
}
