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
        val words = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val newI = words.zip(words.indices).fold(i) { acc, wordAndIndex ->
            val word = wordAndIndex.first
            val index = wordAndIndex.second
            val replacement = word.first() + index.toString() + word.last()
            acc.replace(word, replacement)
        }
        return findFirstAndLastDigit(newI)
    }
}
