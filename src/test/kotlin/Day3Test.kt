import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day3Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "467..114..",
            "...*......",
            "..35..633.",
            "......#...",
            "617*......",
            ".....+.58.",
            "..592.....",
            "......755.",
            "...$.*....",
            ".664.598.."
        )
        val output = Day3.part1(input)
        assertThat(output).isEqualTo("4361")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_3.txt").bufferedReader().readLines()
        val output = Day3.part1(input)
        assertThat(output).isEqualTo("521601")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "467..114..",
            "...*......",
            "..35..633.",
            "......#...",
            "617*......",
            ".....+.58.",
            "..592.....",
            "......755.",
            "...$.*....",
            ".664.598.."
        )
        val output = Day3.part2(input)
        assertThat(output).isEqualTo("467835")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_3.txt").bufferedReader().readLines()
        val output = Day3.part2(input)
        assertThat(output).isEqualTo("80694070")
    }
}
