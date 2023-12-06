import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day6Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "Time:      7  15   30",
            "Distance:  9  40  200"
        )
        val output = Day6.part1(input)
        assertThat(output).isEqualTo("288")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_6.txt").bufferedReader().readLines()
        val output = Day6.part1(input)
        assertThat(output).isEqualTo("625968")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "Time:      7  15   30",
            "Distance:  9  40  200"
        )
        val output = Day6.part2(input)
        assertThat(output).isEqualTo("71503")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_6.txt").bufferedReader().readLines()
        val output = Day6.part2(input)
        assertThat(output).isEqualTo("43663323")
    }
}
