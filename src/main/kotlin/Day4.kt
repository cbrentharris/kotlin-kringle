import kotlin.String
import kotlin.collections.List
import kotlin.math.pow

object Day4 {
    data class CardData(val cardIndex: Int, val winningNumbers: List<Int>, val cardNumbers: Set<Int>) {
        companion object {
            fun parseCardData(input: String): CardData {
                val (cardInformation, numberInformation) = input.split(":")
                val cardIndex = cardInformation.trimStart(*"Card ".toCharArray()).toInt()
                val (winningNumberInformation, cardNumberInformation) = numberInformation.split("|")
                val winningNumbers = parseNumbers(winningNumberInformation)
                val cardNumbers = parseNumbers(cardNumberInformation)
                return CardData(cardIndex, winningNumbers, cardNumbers.toSet())
            }

            fun parseNumbers(numbers: String): List<Int> {
                return numbers.trimStart(*" ".toCharArray())
                    .trimEnd(*" ".toCharArray())
                    .split(" ")
                    .filter(String::isNotEmpty)
                    .map { it.toInt() }
            }
        }

        fun score(): Int {
            val exp = matchingNumbers() - 1
            return 2.0.pow(exp.toDouble()).toInt()
        }

        fun matchingNumbers(): Int {
            return winningNumbers.filter { cardNumbers.contains(it) }.count()
        }
    }

    fun part1(input: List<String>): String = input.map(CardData::parseCardData).map(CardData::score).sum().toString()

    fun part2(input: List<String>): String = findTotalCards(input.map(CardData::parseCardData), emptyMap()).toString()

    fun findTotalCards(cards: List<CardData>, cardMultiplierCount: Map<Int, Int>): Int {
        if (cards.isEmpty()) {
            return 0
        }
        val card = cards.first()
        val multiplier = (cardMultiplierCount[card.cardIndex] ?: 0) + 1
        val updateCardMultiplierCount = (card.cardIndex..card.cardIndex + card.matchingNumbers())
            .map { it to 1 * multiplier }.toMap().toMutableMap()
        cardMultiplierCount.filter { (k, _) -> k > card.cardIndex }.forEach { (key, value) ->
            updateCardMultiplierCount.merge(key, value) { a, b -> a + b }
        }
        return multiplier + findTotalCards(cards.drop(1), updateCardMultiplierCount)
    }
}
