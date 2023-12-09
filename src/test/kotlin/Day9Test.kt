import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day9Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "0 3 6 9 12 15",
            "1 3 6 10 15 21",
            "10 13 16 21 30 45"
        )
        val output = Day9.part1(input)
        assertThat(output).isEqualTo("114")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_9.txt").bufferedReader().readLines()
        val output = Day9.part1(input)
        assertThat(output).isEqualTo("1868368343")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "0 3 6 9 12 15",
            "1 3 6 10 15 21",
            "10 13 16 21 30 45"
        )
        val output = Day9.part2(input)
        assertThat(output).isEqualTo("2")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_9.txt").bufferedReader().readLines()
        val output = Day9.part2(input)
        assertThat(output).isEqualTo("1022")
    }
}
