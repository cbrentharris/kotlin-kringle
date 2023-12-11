import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "...#......",
            ".......#..",
            "#.........",
            "..........",
            "......#...",
            ".#........",
            ".........#",
            "..........",
            ".......#..",
            "#...#....."
        )
        val output = Day11.part1(input)
        assertThat(output).isEqualTo("374")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_11.txt").bufferedReader().readLines()
        val output = Day11.part1(input)
        assertThat(output).isEqualTo("9686930")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "...#......",
            ".......#..",
            "#.........",
            "..........",
            "......#...",
            ".#........",
            ".........#",
            "..........",
            ".......#..",
            "#...#....."
        )
        val output = Day11.part2(input)
        assertThat(output).isEqualTo("82000210")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_11.txt").bufferedReader().readLines()
        val output = Day11.part2(input)
        assertThat(output).isEqualTo("630728425490")
    }
}
