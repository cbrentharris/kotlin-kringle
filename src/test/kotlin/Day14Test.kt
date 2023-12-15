import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day14Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "O....#....",
            "O.OO#....#",
            ".....##...",
            "OO.#O....O",
            ".O.....O#.",
            "O.#..O.#.#",
            "..O..#O..O",
            ".......O..",
            "#....###..",
            "#OO..#...."
        )
        val output = Day14.part1(input)
        assertThat(output).isEqualTo("136")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_14.txt").bufferedReader().readLines()
        val output = Day14.part1(input)
        assertThat(output).isEqualTo("108614")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "O....#....",
            "O.OO#....#",
            ".....##...",
            "OO.#O....O",
            ".O.....O#.",
            "O.#..O.#.#",
            "..O..#O..O",
            ".......O..",
            "#....###..",
            "#OO..#...."
        )
        val output = Day14.part2(input)
        assertThat(output).isEqualTo("64")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_14.txt").bufferedReader().readLines()
        val output = Day14.part2(input)
        assertThat(output).isEqualTo("96447")
    }
}
