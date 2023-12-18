import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "2413432311323",
            "3215453535623",
            "3255245654254",
            "3446585845452",
            "4546657867536",
            "1438598798454",
            "4457876987766",
            "3637877979653",
            "4654967986887",
            "4564679986453",
            "1224686865563",
            "2546548887735",
            "4322674655533"
        )
        val output = Day17.part1(input)
        assertThat(output).isEqualTo("102")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_17.txt").bufferedReader().readLines()
        val output = Day17.part1(input)
        assertThat(output).isLessThan("1356")
            .isLessThan("1004")
            .isEqualTo("1001")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "2413432311323",
            "3215453535623",
            "3255245654254",
            "3446585845452",
            "4546657867536",
            "1438598798454",
            "4457876987766",
            "3637877979653",
            "4654967986887",
            "4564679986453",
            "1224686865563",
            "2546548887735",
            "4322674655533"
        )
        val output = Day17.part2(input)
        assertThat(output).isEqualTo("94")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_17.txt").bufferedReader().readLines()
        val output = Day17.part2(input)
        assertThat(output)
            .isGreaterThan("1188")
            .isEqualTo("1197")
    }
}
