import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

public class Day22Test {
    @Test
    public fun testExamplePart1() {
        val input: List<String> = listOf(
            "1,0,1~1,2,1",
            "0,0,2~2,0,2",
            "0,2,3~2,2,3",
            "0,0,4~0,2,4",
            "2,0,5~2,2,5",
            "0,1,6~2,1,6",
            "1,1,8~1,1,9"
        )
        val output = Day22.part1(input)
        assertThat(output).isEqualTo("5")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_22.txt").bufferedReader().readLines()
        val output = Day22.part1(input)
        assertThat(output.toInt())
            .isLessThan(1102)
            .isLessThan(1096)
            .isGreaterThan(269)
            .isNotEqualTo(371)
            .isNotEqualTo(364)
            .isNotEqualTo(376)
            .isNotEqualTo(780)
            .isNotEqualTo(603)
            .isNotEqualTo(516)
            .isNotEqualTo(704)
            .isEqualTo(534)
    }

    @Test
    public fun testExamplePart2() {
        val input: List<String> = listOf(
            "1,0,1~1,2,1",
            "0,0,2~2,0,2",
            "0,2,3~2,2,3",
            "0,0,4~0,2,4",
            "2,0,5~2,2,5",
            "0,1,6~2,1,6",
            "1,1,8~1,1,9"
        )
        val output = Day22.part2(input)
        assertThat(output).isEqualTo("7")
    }

    @Test
    public fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_22.txt").bufferedReader().readLines()
        val output = Day22.part2(input)
        assertThat(output).isEqualTo("88156")
    }
}
