import kotlin.String
import kotlin.collections.List

object Day11 {
    fun part1(input: List<String>): String {
        val pairs = generatePairs(input)
        val emptyRows = generateEmptyRows(input)
        val emptyColumns = generateEmptyColumns(input)

        return pairs.sumOf { (a, b) ->
            manhattanDistance(a, b) + crossingRows(a, b, emptyRows) + crossingColumns(a, b, emptyColumns)
        }.toString()
    }

    private fun crossingColumns(a: Day10.Coordinate, b: Day10.Coordinate, emptyColumns: Set<Int>): Long {
        return emptyColumns.count { a.x < it && b.x > it || a.x > it && b.x < it }.toLong()
    }

    private fun crossingRows(a: Day10.Coordinate, b: Day10.Coordinate, emptyRows: Set<Int>): Long {
        return emptyRows.count { a.y < it && b.y > it || a.y > it && b.y < it }.toLong()
    }

    private fun manhattanDistance(a: Day10.Coordinate, b: Day10.Coordinate): Long {
        return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y)).toLong()
    }

    fun part2(input: List<String>): String {
        val pairs = generatePairs(input)
        val emptyRows = generateEmptyRows(input)
        val emptyColumns = generateEmptyColumns(input)


        return pairs.sumOf { (a, b) ->
            manhattanDistance(a, b) + crossingRows(a, b, emptyRows) * 999_999L + crossingColumns(
                a,
                b,
                emptyColumns
            ) * 999_999L
        }.toString()
    }

    private fun generatePairs(input: List<String>): List<Pair<Day10.Coordinate, Day10.Coordinate>> {
        val coordinates = input.mapIndexed { y, line ->
            line.mapIndexedNotNull { x, c ->
                if (c == '#') {
                    Day10.Coordinate(x, y)
                } else {
                    null
                }
            }
        }.flatten()
        return coordinates.indices.flatMap { i ->
            (i + 1..<coordinates.size).map { j ->
                Pair(coordinates[i], coordinates[j])
            }
        }
    }

    private fun generateEmptyColumns(input: List<String>): Set<Int> {
        return (0..<input[0].length).filter { col ->
            input.all { it[col] == '.' }
        }.toSet()
    }

    private fun generateEmptyRows(input: List<String>): Set<Int> {
        return input.mapIndexedNotNull { y, line ->
            if (line.all { it == '.' }) {
                y
            } else {
                null
            }
        }.toSet()
    }
}
