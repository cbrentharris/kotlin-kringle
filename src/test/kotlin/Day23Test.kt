import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

public class Day23Test {
    @Test
    public fun testExamplePart1() {
        val input: List<String> = listOf(
            "#.#####################",
            "#.......#########...###",
            "#######.#########.#.###",
            "###.....#.>.>.###.#.###",
            "###v#####.#v#.###.#.###",
            "###.>...#.#.#.....#...#",
            "###v###.#.#.#########.#",
            "###...#.#.#.......#...#",
            "#####.#.#.#######.#.###",
            "#.....#.#.#.......#...#",
            "#.#####.#.#.#########v#",
            "#.#...#...#...###...>.#",
            "#.#.#v#######v###.###v#",
            "#...#.>.#...>.>.#.###.#",
            "#####v#.#.###v#.#.###.#",
            "#.....#...#...#.#.#...#",
            "#.#########.###.#.#.###",
            "#...###...#...#...#.###",
            "###.###.#.###v#####v###",
            "#...#...#.#.>.>.#.>.###",
            "#.###.###.#.###.#.#v###",
            "#.....###...###...#...#",
            "#####################.#"
        )
        val output = Day23.part1(input)
        assertThat(output).isEqualTo("94")
    }

    @Test
    public fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_23.txt").bufferedReader().readLines()
        val output = Day23.part1(input)
        assertThat(output).isEqualTo("2206")
    }

    @Test
    public fun testExamplePart2() {
        val input: List<String> = listOf(
            "#.#####################",
            "#.......#########...###",
            "#######.#########.#.###",
            "###.....#.>.>.###.#.###",
            "###v#####.#v#.###.#.###",
            "###.>...#.#.#.....#...#",
            "###v###.#.#.#########.#",
            "###...#.#.#.......#...#",
            "#####.#.#.#######.#.###",
            "#.....#.#.#.......#...#",
            "#.#####.#.#.#########v#",
            "#.#...#...#...###...>.#",
            "#.#.#v#######v###.###v#",
            "#...#.>.#...>.>.#.###.#",
            "#####v#.#.###v#.#.###.#",
            "#.....#...#...#.#.#...#",
            "#.#########.###.#.#.###",
            "#...###...#...#...#.###",
            "###.###.#.###v#####v###",
            "#...#...#.#.>.>.#.>.###",
            "#.###.###.#.###.#.#v###",
            "#.....###...###...#...#",
            "#####################.#"
        )
        val output = Day23.part2(input)
        assertThat(output).isEqualTo("154")
    }

    @Test
    public fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_23.txt").bufferedReader().readLines()
        val output = Day23.part2(input)
        assertThat(output)
            .isGreaterThan("6086")
            .isEqualTo("6490")
    }
}
