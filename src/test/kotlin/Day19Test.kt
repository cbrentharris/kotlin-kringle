import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class Day19Test {
    @Test
    fun testExamplePart1() {
        val input: List<String> = listOf(
            "px{a<2006:qkq,m>2090:A,rfg}",
            "pv{a>1716:R,A}",
            "lnx{m>1548:A,A}",
            "rfg{s<537:gd,x>2440:R,A}",
            "qs{s>3448:A,lnx}",
            "qkq{x<1416:A,crn}",
            "crn{x>2662:A,R}",
            "in{s<1351:px,qqz}",
            "qqz{s>2770:qs,m<1801:hdj,R}",
            "gd{a>3333:R,R}",
            "hdj{m>838:A,pv}",
            "",
            "{x=787,m=2655,a=1222,s=2876}",
            "{x=1679,m=44,a=2067,s=496}",
            "{x=2036,m=264,a=79,s=2244}",
            "{x=2461,m=1339,a=466,s=291}",
            "{x=2127,m=1623,a=2188,s=1013}"
        )
        val output = Day19.part1(input)
        assertThat(output).isEqualTo("19114")
    }

    @Test
    fun testPart1() {
        val input = javaClass.getResourceAsStream("/day_19.txt").bufferedReader().readLines()
        val output = Day19.part1(input)
        assertThat(output).isEqualTo("346230")
    }

    @Test
    fun testExamplePart2() {
        val input: List<String> = listOf(
            "px{a<2006:qkq,m>2090:A,rfg}",
            "pv{a>1716:R,A}",
            "lnx{m>1548:A,A}",
            "rfg{s<537:gd,x>2440:R,A}",
            "qs{s>3448:A,lnx}",
            "qkq{x<1416:A,crn}",
            "crn{x>2662:A,R}",
            "in{s<1351:px,qqz}",
            "qqz{s>2770:qs,m<1801:hdj,R}",
            "gd{a>3333:R,R}",
            "hdj{m>838:A,pv}",
            "",
            "{x=787,m=2655,a=1222,s=2876}",
            "{x=1679,m=44,a=2067,s=496}",
            "{x=2036,m=264,a=79,s=2244}",
            "{x=2461,m=1339,a=466,s=291}",
            "{x=2127,m=1623,a=2188,s=1013}"
        )
        val output = Day19.part2(input)
        assertThat(output).isEqualTo("167409079868000")
    }

    @Test
    fun testPart2() {
        val input = javaClass.getResourceAsStream("/day_19.txt").bufferedReader().readLines()
        val output = Day19.part2(input)
        assertThat(output).isEqualTo("124693661917133")
    }
}
