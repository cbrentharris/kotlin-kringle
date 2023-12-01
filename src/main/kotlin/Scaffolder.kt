import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Scaffolds a new day of Advent of Code.
 *
 * Usage: `./gradlew scaffold --args <day>`
 *
 * If you want to overwrite existing files, use `./gradlew scaffold --args "<day> force"`
 *
 * This will create a new file in `src/main/kotlin` and `src/test/kotlin` for the given day,
 * and download the input for that day.
 */
fun main(args: Array<String>) {
    val day = args[0].toInt()
    val force = !args.find { it == "force" }.isNullOrEmpty()
    Scaffolder.scaffold(day, force)
}

object Scaffolder {
    fun scaffold(day: Int, force: Boolean) {
        scaffoldMain(day, force)
        scaffoldTest(day, force)
        downloadInput(day)
    }

    private fun downloadInput(day: Int) {
        val client = OkHttpClient()
        // Load the session ID from a config file in the resources directory
        val session = Scaffolder::class.java.getResourceAsStream("/session.txt").bufferedReader().readLine()
        val downloadUrl = "https://adventofcode.com/2023/day/$day/input"
        val request = Request.Builder()
            .url(downloadUrl)
            .addHeader("Cookie", "session=$session")
            .build()
        val response = client.newCall(request).execute()
        /// Save the response to a file called day_$day.txt
        val file = java.io.File("src/test/resources/day_$day.txt")
        file.writeText(response.body?.string() ?: "")
    }

    private fun scaffoldTest(day: Int, force: Boolean) {
        val testClass = ClassName("", "Day${day}Test")
        val file = FileSpec.builder(testClass)
            .addType(
                TypeSpec.Companion.classBuilder(testClass)
                    .addFunction(
                        FunSpec.builder("testExamplePart1")
                            .returns(Unit::class)
                            .addStatement("val input: List<String> = emptyList()")
                            .addStatement("val output = Day$day.part1(input)")
                            .addStatement("assertThat(output).isEqualTo(\"\")")
                            .addAnnotation(org.junit.jupiter.api.Test::class.java)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("testPart1")
                            .returns(Unit::class)
                            .addStatement("val input = javaClass.getResourceAsStream(\"/day_$day.txt\").bufferedReader().readLines()")
                            .addStatement("val output = Day$day.part1(input)")
                            .addStatement("assertThat(output).isEqualTo(\"\")")
                            .addAnnotation(org.junit.jupiter.api.Test::class.java)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("testExamplePart2")
                            .returns(Unit::class)
                            .addStatement("val input: List<String> = emptyList()")
                            .addStatement("val output = Day$day.part2(input)")
                            .addStatement("assertThat(output).isEqualTo(\"\")")
                            .addAnnotation(org.junit.jupiter.api.Test::class.java)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("testPart2")
                            .returns(Unit::class)
                            .addStatement("val input = javaClass.getResourceAsStream(\"/day_$day.txt\").bufferedReader().readLines()")
                            .addStatement("val output = Day$day.part2(input)")
                            .addStatement("assertThat(output).isEqualTo(\"\")")
                            .addAnnotation(org.junit.jupiter.api.Test::class.java)
                            .build()
                    )
                    .build()
            )
            .addImport(org.assertj.core.api.AssertionsForClassTypes::class.java, "assertThat")
            .build()
        val fileToWrite = java.io.File("src/test/kotlin/${testClass.simpleName}.kt")
        if (fileToWrite.exists() && !force) {
            throw IllegalStateException("File already exists: $fileToWrite")
        }
        file.writeTo(java.io.File("src/test/kotlin"))
    }

    private fun scaffoldMain(day: Int, force: Boolean) {
        val dayClass = ClassName("", "Day$day")
        val file = FileSpec.builder(dayClass)
            .addType(
                TypeSpec.Companion.objectBuilder(dayClass)
                    .addFunction(
                        FunSpec.builder("part1")
                            .addParameter("input", List::class.parameterizedBy(String::class))
                            .returns(String::class)
                            .addStatement("return \"\"")
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("part2")
                            .addParameter("input", List::class.parameterizedBy(String::class))
                            .returns(String::class)
                            .addStatement("return \"\"")
                            .build()
                    )
                    .build()
            ).build()
        val fileToWrite = java.io.File("src/main/kotlin/${dayClass.simpleName}.kt")
        if (fileToWrite.exists() && !force) {
            throw IllegalStateException("File already exists: $fileToWrite")
        }
        file.writeTo(java.io.File("src/main/kotlin"))
    }
}