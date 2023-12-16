import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

public class Day16Test {
    @Test
    public fun testExamplePart1() {
        val input: List<String> = listOf(
            ".|...\\....",
            "|.-.\\.....",
            ".....|-...",
            "........|.",
            "..........",
            ".........\\",
            "..../.\\\\..",
            ".-.-/..|..",
            ".|....-|.\\",
            "..//.|...."
        )
        val output = Day16.part1(input)
        assertThat(output).isEqualTo("46")
    }

    @Test
    public fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_16.txt").bufferedReader().readLines()
        val output = Day16.part1(input)
        assertThat(output).isEqualTo("7034")
    }

    @Test
    public fun testExamplePart2() {
        val input: List<String> = listOf(
            ".|...\\....",
            "|.-.\\.....",
            ".....|-...",
            "........|.",
            "..........",
            ".........\\",
            "..../.\\\\..",
            ".-.-/..|..",
            ".|....-|.\\",
            "..//.|...."
        )
        val output = Day16.part2(input)
        assertThat(output).isEqualTo("51")
    }

    @Test
    public fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_16.txt").bufferedReader().readLines()
        val output = Day16.part2(input)
        assertThat(output).isEqualTo("7759")
    }
}
