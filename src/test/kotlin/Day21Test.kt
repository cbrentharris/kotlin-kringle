import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "...........",
            ".....###.#.",
            ".###.##..#.",
            "..#.#...#..",
            "....#.#....",
            ".##..S####.",
            ".##..#...#.",
            ".......##..",
            ".##.#.####.",
            ".##..##.##.",
            "..........."
        )
        val output = Day21.part1(input, 6)
        assertThat(output).isEqualTo("16")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_21.txt").bufferedReader().readLines()
        val output = Day21.part1(input, 64)
        assertThat(output).isEqualTo("3729")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "...........",
            ".....###.#.",
            ".###.##..#.",
            "..#.#...#..",
            "....#.#....",
            ".##..S####.",
            ".##..#...#.",
            ".......##..",
            ".##.#.####.",
            ".##..##.##.",
            "..........."
        )
        assertThat(Day21.part2(input, 6)).isEqualTo("16")
        assertThat(Day21.part2(input, 10)).isEqualTo("50")
        assertThat(Day21.part2(input, 50)).isEqualTo("1594")
        assertThat(Day21.part2(input, 62)).isEqualTo("2479")
        assertThat(Day21.part2(input, 75)).isEqualTo("3687")
        assertThat(Day21.part2(input, 77)).isEqualTo("3836")
        assertThat(Day21.part2(input, 100)).isEqualTo("6536")
        assertThat(Day21.part2(input, 500)).isEqualTo("167004")
        assertThat(Day21.part2(input, 1000)).isEqualTo("668697")
        assertThat(Day21.part2(input, 5000)).isEqualTo("16733044")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_21.txt").bufferedReader().readLines()
        val output = Day21.part2(input, 26501365)
        assertThat(output)
            .isLessThan("621289922916121")
            .isGreaterThan("621289922885889")
            .isEqualTo("621289922886149")
        assertThat(Day21.part2(input, 500)).isEqualTo("222225")
        assertThat(Day21.part2(input, 1000)).isEqualTo("887032")
        assertThat(Day21.part2(input, 2000)).isEqualTo("3540650")
    }


    @Test
    fun testDiamondSize() {
        assertThat(Day21.diamondSize(1)).isEqualTo(1)
        assertThat(Day21.diamondSize(2)).isEqualTo(5)
        assertThat(Day21.diamondSize(3)).isEqualTo(13)
        assertThat(Day21.diamondSize(4)).isEqualTo(25)
    }

    @Test
    fun testOddAndEvenSizes() {
        assertThat(Day21.evenCoordinates(1)).isEqualTo(1)
        assertThat(Day21.oddCoordinates(1)).isEqualTo(0)
        assertThat(Day21.evenCoordinates(2)).isEqualTo(1)
        assertThat(Day21.oddCoordinates(2)).isEqualTo(4)// 4
        assertThat(Day21.evenCoordinates(3)).isEqualTo(9) // 1 + 8
        assertThat(Day21.oddCoordinates(3)).isEqualTo(4)
        assertThat(Day21.evenCoordinates(4)).isEqualTo(9)
        assertThat(Day21.oddCoordinates(4)).isEqualTo(16) // 4 + 12
        assertThat(Day21.evenCoordinates(5)).isEqualTo(25) // 1 + 8 + 16
        assertThat(Day21.oddCoordinates(5)).isEqualTo(16)
        assertThat(Day21.evenCoordinates(6)).isEqualTo(25) // 1 + 8 + 16
        assertThat(Day21.oddCoordinates(6)).isEqualTo(36)// 4 + 12 + 20
    }
}
