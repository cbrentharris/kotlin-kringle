import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day1Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "1abc2n",
            "pqr3stu8vwx",
            "a1b2c3d4e5f",
            "treb7uchet"
        )
        val output = Day1.part1(input)
        assertThat(output).isEqualTo("142")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_1.txt").bufferedReader().readLines()
        val output = Day1.part1(input)
        assertThat(output).isEqualTo("55172")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf("two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2n",
                "zoneight234n",
                "7pqrstsixteen")
        val output = Day1.part2(input)
        assertThat(output).isEqualTo("281")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_1.txt").bufferedReader().readLines()
        val output = Day1.part2(input)
        assertThat(output).isEqualTo("54925")
    }
}
