import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day20Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "broadcaster -> a, b, c",
            "%a -> b",
            "%b -> c",
            "%c -> inv",
            "&inv -> a"
        )
        val output = Day20.part1(input)
        assertThat(output).isEqualTo("32000000")
    }

    @Test
    fun testExample2Part1() {
        val input: List<String> = listOf(
            "broadcaster -> a",
            "%a -> inv, con",
            "&inv -> b",
            "%b -> con",
            "&con -> output"
        )
        val output = Day20.part1(input)
        assertThat(output).isEqualTo("11687500")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_20.txt").bufferedReader().readLines()
        val output = Day20.part1(input)
        assertThat(output).isEqualTo("898731036")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_20.txt").bufferedReader().readLines()
        val output = Day20.part2(input)
        assertThat(output)
            .isGreaterThan("207855915349681") // 3797 ^4
            .isLessThan("250162589607841") // 3979 ^ 4
            .isEqualTo("229414480926893")
    }
}
