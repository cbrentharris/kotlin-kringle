import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day8Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "RL",
            "",
            "AAA = (BBB, CCC)",
            "BBB = (DDD, EEE)",
            "CCC = (ZZZ, GGG)",
            "DDD = (DDD, DDD)",
            "EEE = (EEE, EEE)",
            "GGG = (GGG, GGG)",
            "ZZZ = (ZZZ, ZZZ)"
        )
        val output = Day8.part1(input)
        assertThat(output).isEqualTo("2")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_8.txt").bufferedReader().readLines()
        val output = Day8.part1(input)
        assertThat(output).isEqualTo("19199")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "LR",
            "",
            "11A = (11B, XXX)",
            "11B = (XXX, 11Z)",
            "11Z = (11B, XXX)",
            "22A = (22B, XXX)",
            "22B = (22C, 22C)",
            "22C = (22Z, 22Z)",
            "22Z = (22B, 22B)",
            "XXX = (XXX, XXX)"
        )
        val output = Day8.part2(input)
        assertThat(output).isEqualTo("6")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_8.txt").bufferedReader().readLines()
        val output = Day8.part2(input)
        assertThat(output).isEqualTo("13663968099527")
    }
}
