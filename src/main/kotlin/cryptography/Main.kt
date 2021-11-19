fun main(args: Array<String>) {
    while (true) {
        println("Task (hide, show, exit):")
        when(val command = readLine()) {
            "hide" -> println("Hiding message in image.")
            "show" -> println("Obtaining message from image.")
            "exit" -> {
                println("Bye!")
                break
            }
            else -> println("Wrong task: $command")
        }
    }
}