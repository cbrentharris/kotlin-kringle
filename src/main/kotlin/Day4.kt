import kotlin.String
import kotlin.collections.List
import kotlin.math.pow

object Day4 {
    data class CardData(val cardIndex: Int, val winningNumberCount: Int) {
        companion object {
            fun parseCardData(input: String): CardData {
                val (cardInformation, numberInformation) = input.split(":")
                val cardIndex = cardInformation.trimStart(*"Card ".toCharArray()).toInt()
                val (winningNumberInformation, cardNumberInformation) = numberInformation.split("|")
                val winningNumbers = parseNumbers(winningNumberInformation)
                val cardNumbers = parseNumbers(cardNumberInformation)
                val winningNumberCount = cardNumbers.filter { winningNumbers.contains(it) }.count()
                return CardData(cardIndex, winningNumberCount)
            }

            private fun parseNumbers(numbers: String): List<Int> {
                return numbers.trimStart(*" ".toCharArray())
                    .trimEnd(*" ".toCharArray())
                    .split(" ")
                    .filter(String::isNotEmpty)
                    .map { it.toInt() }
            }
        }

        fun score(): Int {
            val exp = winningNumberCount - 1
            return 2.0.pow(exp.toDouble()).toInt()
        }

        fun cardIndexesToCopy(): IntRange {
            return (cardIndex + 1..cardIndex + winningNumberCount)
        }
    }

    fun part1(input: List<String>): String = input.map(CardData::parseCardData).map(CardData::score).sum().toString()

    fun part2(input: List<String>): String = findTotalCards(input.map(CardData::parseCardData)).toString()

    private fun findTotalCards(cards: List<CardData>): Int {
        val cardMultiplierCount = mutableMapOf<Int, Int>()
        return cards.map {
            val multiplier = (cardMultiplierCount[it.cardIndex] ?: 0) + 1
            it.cardIndexesToCopy().forEach {
                cardMultiplierCount.merge(it, multiplier) { a, b -> a + b }
            }
            multiplier
        }.sum()
    }
}
