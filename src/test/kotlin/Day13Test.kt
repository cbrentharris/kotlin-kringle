import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "#.##..##.",
            "..#.##.#.",
            "##......#",
            "##......#",
            "..#.##.#.",
            "..##..##.",
            "#.#.##.#.",
            "",
            "#...##..#",
            "#....#..#",
            "..##..###",
            "#####.##.",
            "#####.##.",
            "..##..###",
            "#....#..#"
        )
        val output = Day13.part1(input)
        assertThat(output).isEqualTo("405")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_13.txt").bufferedReader().readLines()
        val output = Day13.part1(input)
        assertThat(output).isEqualTo("30802")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "#.##..##.",
            "..#.##.#.",
            "##......#",
            "##......#",
            "..#.##.#.",
            "..##..##.",
            "#.#.##.#.",
            "",
            "#...##..#",
            "#....#..#",
            "..##..###",
            "#####.##.",
            "#####.##.",
            "..##..###",
            "#....#..#"
        )
        val output = Day13.part2(input)
        assertThat(output).isEqualTo("400")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_13.txt").bufferedReader().readLines()
        val output = Day13.part2(input)
        assertThat(output).isEqualTo("37876")
    }
}
