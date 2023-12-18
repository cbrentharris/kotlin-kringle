import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "R 6 (#70c710)",
            "D 5 (#0dc571)",
            "L 2 (#5713f0)",
            "D 2 (#d2c081)",
            "R 2 (#59c680)",
            "D 2 (#411b91)",
            "L 5 (#8ceee2)",
            "U 2 (#caa173)",
            "L 1 (#1b58a2)",
            "U 2 (#caa171)",
            "R 2 (#7807d2)",
            "U 3 (#a77fa3)",
            "L 2 (#015232)",
            "U 2 (#7a21e3)"
        )
        val output = Day18.part1(input)
        assertThat(output).isEqualTo("62")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_18.txt").bufferedReader().readLines()
        val output = Day18.part1(input)
        assertThat(output)
            .isLessThan("36552")
            .isEqualTo("34329")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "R 6 (#70c710)",
            "D 5 (#0dc571)",
            "L 2 (#5713f0)",
            "D 2 (#d2c081)",
            "R 2 (#59c680)",
            "D 2 (#411b91)",
            "L 5 (#8ceee2)",
            "U 2 (#caa173)",
            "L 1 (#1b58a2)",
            "U 2 (#caa171)",
            "R 2 (#7807d2)",
            "U 3 (#a77fa3)",
            "L 2 (#015232)",
            "U 2 (#7a21e3)"
        )
        val output = Day18.part2(input)
        assertThat(output).isEqualTo("952408144115")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_18.txt").bufferedReader().readLines()
        val output = Day18.part2(input)
        assertThat(output).isEqualTo("42617947302920")
    }
}
