import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "..F7.",
            ".FJ|.",
            "SJ.L7",
            "|F--J",
            "LJ..."
        )
        val output = Day10.part1(input)
        assertThat(output).isEqualTo("8")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_10.txt").bufferedReader().readLines()
        val output = Day10.part1(input)
        assertThat(output).isEqualTo("6820")
    }

    @Test
    fun testExample1Part2() {
        val input: List<String> = listOf(
            ".F----7F7F7F7F-7....",
            ".|F--7||||||||FJ....",
            ".||.FJ||||||||L7....",
            "FJL7L7LJLJ||LJ.L-7..",
            "L--J.L7...LJS7F-7L7.",
            "....F-J..F7FJ|L7L7L7",
            "....L7.F7||L7|.L7L7|",
            ".....|FJLJ|FJ|F7|.LJ",
            "....FJL-7.||.||||...",
            "....L---J.LJ.LJLJ..."
        )
        val output = Day10.part2(input)
        assertThat(output).isEqualTo("8")
    }

    @Test
    fun testExample2Part2() {
        val input2 = listOf(
            "...........",
            ".S-------7.",
            ".|F-----7|.",
            ".||.....||.",
            ".||.....||.",
            ".|L-7.F-J|.",
            ".|..|.|..|.",
            ".L--J.L--J.",
            "..........."
        )

        val output2 = Day10.part2(input2)
        assertThat(output2).isEqualTo("4")
    }

    @Test
    fun testExample3Part2() {
        val input3 = listOf(
            "..........",
            ".S------7.",
            ".|F----7|.",
            ".||....||.",
            ".||....||.",
            ".|L-7F-J|.",
            ".|..||..|.",
            ".L--JL--J.",
            ".........."
        )

        val output3 = Day10.part2(input3)
        assertThat(output3).isEqualTo("4")
    }

    @Test
    fun testExample4Part2() {
        val input4 = listOf(
            "FF7FSF7F7F7F7F7F---7",
            "L|LJ||||||||||||F--J",
            "FL-7LJLJ||||||LJL-77",
            "F--JF--7||LJLJ7F7FJ-",
            "L---JF-JLJ.||-FJLJJ7",
            "|F|F-JF---7F7-L7L|7|",
            "|FFJF7L7F-JF7|JL---7",
            "7-L-JL7||F7|L7F-7F7|",
            "L.L7LFJ|||||FJL7||LJ",
            "L7JLJL-JLJLJL--JLJ.L"
        )

        val output4 = Day10.part2(input4)
        assertThat(output4).isEqualTo("10")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_10.txt").bufferedReader().readLines()
        val output = Day10.part2(input)
        assertThat(output)
            .isLessThan("545")
            .isLessThan("540")
            .isLessThan("500")
            .isNotEqualTo("130")
            .isEqualTo("337")
    }
}
