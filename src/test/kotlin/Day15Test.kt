import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day15Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")
        val output = Day15.part1(input)
        assertThat(output).isEqualTo("1320")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_15.txt").bufferedReader().readLines()
        val output = Day15.part1(input)
        assertThat(output).isEqualTo("505379")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")
        val output = Day15.part2(input)
        assertThat(output).isEqualTo("145")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_15.txt").bufferedReader().readLines()
        val output = Day15.part2(input)
        assertThat(output).isLessThan("521020").isEqualTo("263211")
    }
}
