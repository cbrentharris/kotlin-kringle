import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "???.### 1,1,3",
            ".??..??...?##. 1,1,3",
            "?#?#?#?#?#?#?#? 1,3,1,6",
            "????.#...#... 4,1,1",
            "????.######..#####. 1,6,5",
            "?###???????? 3,2,1"
        )
        val output = Day12.part1(input)
        assertThat(output).isEqualTo("21")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_12.txt").bufferedReader().readLines()
        val output = Day12.part1(input)
        assertThat(output)
            .isGreaterThan("6689")
            .isEqualTo("7792")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "???.### 1,1,3",
            ".??..??...?##. 1,1,3",
            "?#?#?#?#?#?#?#? 1,3,1,6",
            "????.#...#... 4,1,1",
            "????.######..#####. 1,6,5",
            "?###???????? 3,2,1"
        )
        val output = Day12.part2(input)
        assertThat(output).isEqualTo("525152")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_12.txt").bufferedReader().readLines()
        val output = Day12.part2(input)
        assertThat(output).isEqualTo("13012052341533")
    }
}
