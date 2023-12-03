import kotlin.String
import kotlin.collections.List

object Day3 {
    data class NumberPosition(
        val number: Int,
        val start: Int,
        val end: Int,
        val row: Int
    )

    data class Symbol(val symbol: Char, val coordinate: Coordinate)

    data class Coordinate(val x: Int, val y: Int)

    private val reservedSymbol = ".".first()

    fun part1(input: List<String>): String {
        val numbers = parseNumbers(input)
        val specialCharacters = parseSpecialCharacters(input).associateBy { it.coordinate }
        val partNumbers = numbers.filter {
            val (_, start, end, row) = it
            val up = row - 1
            val down = row + 1
            val left = start - 1
            val right = end + 1
            val coordinateCandidates = (left..right).flatMap { x ->
                listOf(
                    Coordinate(x, up),
                    Coordinate(x, down)
                )
            } + listOf(Coordinate(left, row), Coordinate(right, row))
            coordinateCandidates.any { specialCharacters.containsKey(it) }
        }
        return partNumbers.sumOf { it.number }.toString()
    }

    private fun parseNumbers(input: List<String>): List<NumberPosition> {
        return input.zip(input.indices).flatMap { (line, rowIndex) ->
            val charArray = line.toCharArray()
            val withIndexes = charArray.zip(charArray.indices)
            val deque = ArrayDeque(withIndexes)
            var buffer = ""
            var start = 0
            var end = 0
            val numbers = mutableListOf<NumberPosition>()
            while (deque.isNotEmpty()) {
                val (char, index) = deque.removeFirst()
                if (char.isDigit()) {
                    end = index
                    buffer += char
                } else {
                    if (buffer.isNotEmpty()) {
                        numbers.add(NumberPosition(buffer.toInt(), start, end, rowIndex))
                    }
                    buffer = ""
                    start = index + 1
                    end = index + 1
                }
            }
            if (buffer.isNotEmpty()) {
                numbers.add(NumberPosition(buffer.toInt(), start, end, rowIndex))
            }
            numbers
        }
    }

    private fun parseSpecialCharacters(input: List<String>): List<Symbol> {
        val validSymbols = input.map { it.toCharArray() }
            .map { it.zip(it.indices) }
            .map { it.filter { !it.first.isDigit() }.filter { it.first != reservedSymbol } }
        return validSymbols.zip(validSymbols.indices)
            .flatMap { (row, rowIndex) -> row.map { Symbol(it.first, Coordinate(it.second, rowIndex)) } }
    }


    fun part2(input: List<String>): String {
        val numbers = parseNumbers(input)
        val specialCharacters = parseSpecialCharacters(input)
        val gears = specialCharacters.filter { it.symbol == '*' }.associateBy { it.coordinate }
        return gears.map { adjacent(it.key, numbers) }
            .filter { it.size == 2 }
            .map { it.map { it.number }.reduce { a, b -> a * b } }
            .sum().toString()
    }

    private fun adjacent(it: Coordinate, numbers: List<NumberPosition>): List<NumberPosition> {
        val up = Coordinate(it.x, it.y - 1)
        val down = Coordinate(it.x, it.y + 1)
        val left = Coordinate(it.x - 1, it.y)
        val right = Coordinate(it.x + 1, it.y)
        val upLeft = Coordinate(it.x - 1, it.y - 1)
        val upRight = Coordinate(it.x + 1, it.y - 1)
        val downLeft = Coordinate(it.x - 1, it.y + 1)
        val downRight = Coordinate(it.x + 1, it.y + 1)
        val candidates = listOf(up, down, left, right, upLeft, upRight, downLeft, downRight)
        return numbers.filter { num -> candidates.any { it.x <= num.end && it.x >= num.start && it.y == num.row } }
    }
}
