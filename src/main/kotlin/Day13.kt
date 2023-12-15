import kotlin.String
import kotlin.collections.List

object Day13 {
    private const val NORMAL_MULTIPLIER = 100
    private const val TRANSPOSED_MULTIPLIER = 1

    fun part1(input: List<String>): String {
        val grids = parseGrids(input)

        return grids.sumOf { grid ->
            // Transposed is actually columns
            val transposedGrid = transpose(grid)
            scoreExact(grid, NORMAL_MULTIPLIER) + scoreExact(transposedGrid, TRANSPOSED_MULTIPLIER)
        }.toString()

    }

    private fun scoreExact(grid: List<String>, multiplier: Int): Int {
        return findLinesOfExactness(grid).find { isMirror(it, grid) }?.let {
            // Normal is rows, so rows above
            val rowsAbove = it.first + 1
            return rowsAbove * multiplier
        } ?: 0
    }

    private fun isMirror(it: Pair<Int, Int>, grid: List<String>): Boolean {
        val (a, b) = it
        if (a < 0 || b >= grid.size) {
            return true
        }
        return grid[a] == grid[b] && isMirror(a - 1 to b + 1, grid)
    }

    /**
     * Return the rows in which there is a line of exactness
     */
    private fun findLinesOfExactness(grid: List<String>): List<Pair<Int, Int>> {
        return grid.withIndex().zipWithNext().filter { (a, b) -> a.value == b.value }
            .map { (a, b) -> a.index to b.index }
    }

    fun transpose(grid: List<String>): List<String> {
        val transposedGrid = List(grid[0].length) { CharArray(grid.size) }
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                transposedGrid[j][i] = grid[i][j]
            }
        }
        return transposedGrid.map { it.joinToString("") }
    }

    private fun parseGrids(input: List<String>): List<List<String>> {
        val emptyIndexes =
            listOf(-1) + input.withIndex().filter { (_, b) -> b.isBlank() }.map { (i, _) -> i } + listOf(input.size)
        return emptyIndexes.zipWithNext().map { (a, b) -> input.subList(a + 1, b) }
    }

    fun part2(input: List<String>): String {
        val grids = parseGrids(input)
        return grids.sumOf { grid ->
            // Transposed is actually columns
            val transposedGrid = transpose(grid)
            scoreSmudged(grid, NORMAL_MULTIPLIER) + scoreSmudged(transposedGrid, TRANSPOSED_MULTIPLIER)
        }.toString()
    }

    private fun scoreSmudged(grid: List<String>, multiplier: Int): Int {
        val rowsOfMaybeExactness = findLinesOfExactness(grid) + findLinesOffByOne(grid)
        return rowsOfMaybeExactness.find { isAlmostMirror(it, grid, 0) }?.let {
            // Normal is rows, so rows above
            val rowsAbove = it.first + 1
            rowsAbove * multiplier
        } ?: 0
    }

    private fun findLinesOffByOne(grid: List<String>): List<Pair<Int, Int>> {
        return grid.withIndex().zipWithNext().filter { (a, b) -> isOffByOne(a.value, b.value) }
            .map { (a, b) -> a.index to b.index }
    }

    private fun isAlmostMirror(it: Pair<Int, Int>, grid: List<String>, offByOneCount: Int): Boolean {
        if (offByOneCount > 1) {
            return false
        }
        val (a, b) = it
        if (a < 0 || b >= grid.size) {
            return offByOneCount == 1
        }
        val offByOne = isOffByOne(grid[a], grid[b])
        val offByMoreThanOne = !offByOne && grid[a] != grid[b]
        if (offByMoreThanOne) {
            return false
        }
        val newCount = if (offByOne) {
            offByOneCount + 1
        } else {
            offByOneCount
        }
        return isAlmostMirror(a - 1 to b + 1, grid, newCount)
    }

    private fun isOffByOne(a: String, b: String): Boolean {
        return a.toCharArray().zip(b.toCharArray())
            .count { (a, b) -> a != b } == 1
    }
}
