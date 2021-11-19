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

        println("Input Image: $infileName")
        println("Output Image: $outfileName")
        val image : BufferedImage = ImageIO.read(inImageFile)
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                val color = Color(image.getRGB(i, j))
                val rgb = Color(
                    color.red or 1,
                    color.green or 1,
                    setLeastSignificantBitToOne(color.blue)
                ).rgb
                image.setRGB(i, j, rgb)
            }
        }

        ImageIO.write(image, "png", outImageFile)
        println("Image $outfileName is saved.")
    } catch (e :Exception) {
        println("Can't read input file!")
    }
}

fun setLeastSignificantBitToOne(pixel: Int) : Int {
    return if (pixel % 2 == 0) pixel + 1
    else pixel
}
