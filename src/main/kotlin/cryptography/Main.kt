package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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
            "show" -> println("Obtaining message from image.")
            "exit" -> {
                println("Bye!")
                break
            }
            else -> println("Wrong task: $command")
        }
    }
}

fun hide() {
    try {
        println("Input image file:")
        val infileName = readLine()!!.toString()
        val inImageFile = File(infileName)
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
        ImageIO.write(inputImage, "png", outImageFile)
        println("Message saved in $outfileName image.")

    } catch (e :Exception) {
        println("Can't read input file!")
    }
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

fun setLeastSignificantBitToOne(pixel: Int) : Int {
    return if (pixel % 2 == 0) pixel + 1
    else pixel
}
