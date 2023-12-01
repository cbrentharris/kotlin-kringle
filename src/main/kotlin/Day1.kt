import kotlin.String
import kotlin.collections.List

object Day1 {
    fun part1(input: List<String>): String {
        return input.sumOf { i -> findFirstAndLastDigit(i) }.toString()
    }

    private fun findFirstAndLastDigit(i: String): Int {
        val digits = i.filter { it.isDigit() }
        return (digits.take(1) + digits.takeLast(1)).toInt()
    }

    fun part2(input: List<String>): String {
        return input.sumOf { i -> findFirstAndLastDigitV2(i) }.toString()
    }

    private fun findFirstAndLastDigitV2(i: String): Int {
        val wordsToReplacements = mapOf(
            "zero" to "0o",
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "4",
            "five" to "5e",
            "six" to "6",
            "seven" to "7n",
            "eight" to "e8t",
            "nine" to "9"
        )
        val newI = wordsToReplacements.toList().fold(i) { acc, word -> acc.replace(word.first, word.second) }
        return findFirstAndLastDigit(newI)
    }
}
