import kotlin.String
import kotlin.collections.List

object Day1 {
    fun part1(input: List<String>): String = input.sumOf(::findFirstAndLastDigit).toString()

    private fun findFirstAndLastDigit(i: String): Int {
        val digits = i.filter { it.isDigit() }
        return (digits.take(1) + digits.takeLast(1)).toInt()
    }

    fun part2(input: List<String>): String = input.sumOf(::findFirstAndLastDigitV2).toString()

    private fun findFirstAndLastDigitV2(i: String): Int {
        val words = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val newI = words.zip(words.indices)
            .map { (word, index) -> word to word.first() + index.toString() + word.last() }
            .fold(i) { acc, (word, replacement) -> acc.replace(word, replacement) }
        return findFirstAndLastDigit(newI)
    }
}
