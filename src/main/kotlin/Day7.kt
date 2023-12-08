import kotlin.String
import kotlin.collections.List

object Day7 {

    interface CardHand : Comparable<CardHand> {
        val maxSimilarCards: Int
        val bid: Long
        val secondMaxSimilarCards: Int
        val cards: CharArray
        val cardValues: Map<Char, Int>

        fun score(rank: Int): Long {
            return bid * rank
        }

        override fun compareTo(other: CardHand): Int {
            return compareBy<CardHand> { it.maxSimilarCards }
                .thenBy { it.secondMaxSimilarCards }
                .then { first, second ->
                    val thisCardValues = first.cards.map { cardValues[it] ?: 0 }
                    val otherCardValues = second.cards.map { cardValues[it] ?: 0 }
                    thisCardValues.zip(otherCardValues).map { it.first - it.second }.firstOrNull { it != 0 } ?: 0
                }
                .compare(this, other)
        }
    }

    data class CamelCardHandWithJokers(override val cards: CharArray, override val bid: Long) : CardHand {
        override val maxSimilarCards: Int
        private val possibleCards: CharArray =
            arrayOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').toCharArray()
        override val secondMaxSimilarCards: Int
        override val cardValues = possibleCards.zip(possibleCards.indices).toMap()

        init {
            val numJokers = cards.count { it == 'J' }
            val nonJokers = cards.filter { it != 'J' }.groupBy { it }.mapValues { it.value.size }
            val values = nonJokers.values.sortedDescending()
            val maxNonJokers = values.firstOrNull() ?: 0
            val secondMaxNonJokers = values.drop(1).firstOrNull() ?: 0
            maxSimilarCards = maxNonJokers + numJokers
            secondMaxSimilarCards = secondMaxNonJokers
        }

        companion object {
            fun parse(input: String): CamelCardHandWithJokers {
                val (cards, bid) = input.split(" ")
                return CamelCardHandWithJokers(cards.toCharArray(), bid.toLong())
            }
        }
    }


    data class CamelCardHand(override val cards: CharArray, override val bid: Long) : CardHand {
        override val maxSimilarCards: Int
        override val secondMaxSimilarCards: Int
        private val possibleCards: CharArray =
            arrayOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A').toCharArray()
        override val cardValues = possibleCards.zip(possibleCards.indices).toMap()

        init {
            val grouped = cards.groupBy { it }.mapValues { it.value.size }
            val values = grouped.values.sortedDescending()
            maxSimilarCards = values.first()
            secondMaxSimilarCards = values.drop(1).firstOrNull() ?: 0
        }

        companion object {
            fun parse(input: String): CamelCardHand {
                val (cards, bid) = input.split(" ")
                return CamelCardHand(cards.toCharArray(), bid.toLong())
            }
        }
    }

    fun part1(input: List<String>): String {
        val hands = input.map(CamelCardHand::parse)
        return score(hands).toString()
    }

    fun part2(input: List<String>): String {
        val hands = input.map(CamelCardHandWithJokers::parse)
        return score(hands).toString()
    }

    private fun score(hands: List<CardHand>): Long {
        val sortedHands = hands.sorted()
        return sortedHands.zip(sortedHands.indices).sumOf { (hand, index) -> hand.score(index + 1) }
    }
}
