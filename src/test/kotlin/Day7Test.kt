import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day7Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "32T3K 765",
            "T55J5 684",
            "KK677 28",
            "KTJJT 220",
            "QQQJA 483"
        )
        val output = Day7.part1(input)
        assertThat(output).isEqualTo("6440")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_7.txt").bufferedReader().readLines()
        val output = Day7.part1(input)
        assertThat(output)
            .isLessThan("249604716")
            .isGreaterThan("249373402")
            .isEqualTo("249483956")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "32T3K 765",
            "T55J5 684",
            "KK677 28",
            "KTJJT 220",
            "QQQJA 483"
        )
        val output = Day7.part2(input)
        assertThat(output).isEqualTo("5905")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_7.txt").bufferedReader().readLines()
        val output = Day7.part2(input)
        assertThat(output)
            .isLessThan("252283830")
            .isEqualTo("252137472")
    }
}
