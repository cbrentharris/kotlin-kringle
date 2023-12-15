import Day13.transpose
import kotlin.String
import kotlin.collections.List

object Day14 {
    private const val ROUNDED = 'O'
    fun part1(input: List<String>): String {
        val transposedDish = transpose(input)
        return transposedDish.sumOf { row ->
            val rolled = roll(row)
            rolled.reversed().withIndex().filter { (_, char) -> char == ROUNDED }.sumOf { (idx, _) -> idx + 1 }
        }.toString()
    }

    private fun roll(row: String): String {
        if (row.isBlank()) {
            return row
        }
        if (row[0] == ROUNDED) {
            return row[0] + roll(row.substring(1))
        }
        val indexToSwap = row.withIndex().dropWhile { (_, char) -> char == '.' }.firstOrNull()
        if (indexToSwap == null) {
            return row
        }
        val (idx, _) = indexToSwap
        return if (row[idx] == ROUNDED) {
            ROUNDED.toString() + roll(row.substring(1, idx) + "." + row.substring(idx + 1))
        } else {
            row.substring(0, idx + 1) + roll(row.substring(idx + 1))
        }
    }

    fun part2(input: List<String>): String {
        val cycled = cycleUntil(transpose(input), 1_000_000_000)
        return cycled
            .sumOf { row ->
                val rowSum =
                    row.reversed().withIndex().filter { (_, char) -> char == ROUNDED }.sumOf { (idx, _) -> idx + 1 }
                rowSum
            }
            .toString()
    }

    private fun cycleUntil(grid: List<String>, targetCycles: Int): List<String> {
        val seenMap = mutableMapOf<List<String>, Long>()
        var cycles = 0L
        var cycled = cycle(grid)
        while (cycles < targetCycles) {
            cycled = cycle(cycled)
            cycles++
            if (seenMap.containsKey(cycled)) {
                val firstSeen = seenMap[cycled]!!
                val cyclesSince = cycles - firstSeen
                var remainingCycles = (targetCycles - cycles) % cyclesSince - 1

                while (remainingCycles > 0) {
                    cycled = cycle(cycled)
                    remainingCycles--
                }
                break
            } else {
                seenMap[cycled] = cycles
            }
        }
        return cycled
    }

    private fun cycle(grid: List<String>): List<String> {
        val northRoll = grid.map { roll(it) }
        val west = transpose(northRoll)
        val westRoll = west.map { roll(it) }
        val south = transpose(westRoll).map { it.reversed() }
        val southRoll = south.map { roll(it) }
        val east = transpose(southRoll.map { it.reversed() }).map { it.reversed() }
        val eastRoll = east.map { roll(it) }
        val backNorth = transpose(eastRoll.map { it.reversed() })
        return backNorth
    }
}
