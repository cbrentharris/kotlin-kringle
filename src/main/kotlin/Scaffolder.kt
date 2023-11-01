/**
 * Scaffolds a new day of Advent of Code.
 *
 * Usage: `./gradlew scaffold --args <day>`
 *
 * This will create a new file in `src/main/kotlin` and `src/test/kotlin` for the given day,
 * and download the input for that day.
 */
fun main(args: Array<String>) {
    val day = args[0].toInt()
}

object Scaffolder {
    fun scaffold(day: Int) {
        scaffoldMain(day)
        scaffoldTest(day)
        downloadInput(day)
    }

    private fun downloadInput(day: Int) {
        TODO("Not yet implemented")
    }

    private fun scaffoldTest(day: Int) {
        TODO("Not yet implemented")
    }

    private fun scaffoldMain(day: Int) {
        TODO("Not yet implemented")
    }
}