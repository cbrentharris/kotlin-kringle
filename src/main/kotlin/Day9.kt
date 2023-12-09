import kotlin.String
import kotlin.collections.List

object Day9 {
    fun part1(input: List<String>): String {
        return parse(input)
            .sumOf { extrapolate(it) { deltas, ret -> deltas.last() + ret } }.toString()
    }

    fun part2(input: List<String>): String {
        return parse(input)
            .sumOf { extrapolate(it) { deltas, ret -> deltas.first() - ret } }.toString()
    }

    private fun parse(input: List<String>): List<List<Long>> {
        return input.map { it.split("\\s+".toRegex()).map { it.toLong() } }
    }

    private fun extrapolate(input: List<Long>, extrapolator: (List<Long>, Long) -> Long): Long {
        val deltas = input.zipWithNext().map { (a, b) -> b - a }
        if (deltas.all { it == 0L }) {
            return extrapolator(input, 0)
        }
        return extrapolator(input, extrapolate(deltas, extrapolator))
    }
}
