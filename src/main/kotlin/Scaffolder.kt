import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import okhttp3.OkHttpClient
import okhttp3.Request

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
    Scaffolder.scaffold(day)
}

object Scaffolder {
    fun scaffold(day: Int) {
        scaffoldMain(day)
        scaffoldTest(day)
        downloadInput(day)
    }

    private fun downloadInput(day: Int) {
        val client = OkHttpClient()
        // Load the session ID from a config file in the resources directory
        val session = Scaffolder::class.java.getResourceAsStream("/session.txt").bufferedReader().readLine()
        val downloadUrl = "https://adventofcode.com/2022/day/$day/input"
        val request = Request.Builder()
            .url(downloadUrl)
            .addHeader("Cookie", "session=$session")
            .build()
        val response = client.newCall(request).execute()
        /// Save the response to a file called day_$day.txt
        val file = java.io.File("src/main/resources/day_$day.txt")
        file.writeText(response.body?.string() ?: "")
    }

    private fun scaffoldTest(day: Int) {
        val testClass = ClassName("", "Day${day}Test")
        //TODO("Not yet implemented")
    }

    private fun scaffoldMain(day: Int) {
        val dayClass = ClassName("", "Day$day")
        val file = FileSpec.builder(dayClass)
            .addType(TypeSpec.Companion.classBuilder(dayClass)
                .addFunction(FunSpec.builder("part1")
                    .addParameter("input", String::class)
                    .returns(String::class)
                    .addStatement("return input")
                    .build())
                .addFunction(FunSpec.builder("part2")
                    .addParameter("input", String::class)
                    .returns(String::class)
                    .addStatement("return input")
                    .build())
                .build()).build()
        file.writeTo(java.io.File("src/main/kotlin"))
    }
}