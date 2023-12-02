import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
        )
        val output = Day2.part1(input)
        assertThat(output).isEqualTo("8")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_2.txt").bufferedReader().readLines()
        val output = Day2.part1(input)
        assertThat(output).isEqualTo("2149")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
        )
        val output = Day2.part2(input)
        assertThat(output).isEqualTo("2286")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_2.txt").bufferedReader().readLines()
        val output = Day2.part2(input)
        assertThat(output).isEqualTo("71274")
    }
}
