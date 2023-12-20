import kotlin.String
import kotlin.collections.List

object Day19 {
    private const val MIN_STATES = 1L
    private const val MAX_STATES = 4000L
    private const val ACCEPTED_STATE = "A"
    private const val REJECTED_STATE = "R"
    private const val START_STATE = "in"

    data class Part(val ratings: Map<String, Int>) {
        companion object {
            fun parse(input: String): Part {
                val ratings = input.drop(1).dropLast(1).split(",").map {
                    val (id, rating) = it.split("=")
                    id to rating.toInt()
                }.toMap()
                return Part(ratings)
            }
        }

        fun score(): Int = ratings.values.sum()
    }

    data class Rule(val key: String, val op: String, val value: Int, val nextState: String) {
        fun inverse(): Rule {
            val inverseOp = when (op) {
                "<" -> ">="
                ">" -> "<="
                else -> throw IllegalArgumentException("Cannot inverse op $op")
            }
            return Rule(key, inverseOp, value, nextState)
        }
    }

    data class Condition(val key: String, val op: String, val value: Int) {
        fun toMinAndMax(): MinAndMax {
            return when (op) {
                "<" -> MinAndMax(MIN_STATES, value.toLong() - 1, key)
                "<=" -> MinAndMax(MIN_STATES, value.toLong(), key)
                ">" -> MinAndMax(value.toLong() + 1, MAX_STATES, key)
                ">=" -> MinAndMax(value.toLong(), MAX_STATES, key)
                else -> throw IllegalArgumentException("Cannot inverse op $op")
            }
        }
    }

    data class CompositeCondition(val conditions: List<Condition>, val nextState: String)

    data class PathFinder(val jobs: Map<String, Job>) {
        fun findPathsToRejectedState(): List<Path> {
            val startJob = jobs[START_STATE]!!
            val paths = mutableListOf<Path>()
            paths.addAll(startJob.conditionsLeadingToRejectedState().map { Path(listOf(it)) })
            val nextStates = ArrayDeque(
                startJob.nonTerminalConditions()
                    .map { it to Path(listOf(it)) })
            while (nextStates.isNotEmpty()) {
                val (nextRule, runningPath) = nextStates.removeFirst()
                val nextJob = jobs[nextRule.nextState]!!
                val failureStates = nextJob.conditionsLeadingToRejectedState()
                val failedPaths = failureStates.map { runningPath.with(it) }
                paths.addAll(failedPaths)
                val nonTerminalStates = nextJob.nonTerminalConditions()
                nextStates.addAll(nonTerminalStates.map { it to runningPath.with(it) })
            }
            return paths
        }
    }

    data class Path(val compositeConditions: List<CompositeCondition>) {
        fun with(compositeCondition: CompositeCondition): Path {
            return Path(compositeConditions + compositeCondition)
        }
    }

    data class Job(
        val id: String,
        val rules: List<Pair<(Part) -> Boolean, String>>,
        val endState: String,
        val rawRules: List<Rule>
    ) {

        fun evaluate(part: Part): String {
            return rules.find { (rule, _) -> rule(part) }?.second ?: endState
        }

        fun conditionsLeadingToRejectedState(): List<CompositeCondition> {
            val rejectedRules = mutableListOf<CompositeCondition>()
            if (endState == REJECTED_STATE) {
                val endStateCompositeCondition =
                    CompositeCondition(
                        rawRules.map { it.inverse() }.map { Condition(it.key, it.op, it.value) },
                        REJECTED_STATE
                    )
                rejectedRules.add(endStateCompositeCondition)
            }
            rejectedRules.addAll(rawRules.withIndex().filter { (_, rule) -> rule.nextState == REJECTED_STATE }
                .map { (idx, rule) ->
                    val inversedPreceedingRules =
                        rawRules.subList(0, idx).map { it.inverse() }.map { Condition(it.key, it.op, it.value) }
                    val thisRule = listOf(Condition(rule.key, rule.op, rule.value))
                    CompositeCondition(inversedPreceedingRules + thisRule, rule.nextState)
                })
            return rejectedRules
        }

        fun nonTerminalConditions(): List<CompositeCondition> {
            val nonAcceptedNorRejectedConditions = mutableListOf<CompositeCondition>()
            if (endState != ACCEPTED_STATE && endState != REJECTED_STATE) {
                val endStateCompositeCondition =
                    CompositeCondition(
                        rawRules.map { it.inverse() }.map { Condition(it.key, it.op, it.value) },
                        endState
                    )
                nonAcceptedNorRejectedConditions.add(endStateCompositeCondition)
            }
            nonAcceptedNorRejectedConditions.addAll(
                rawRules.withIndex()
                    .filter { (_, rule) -> rule.nextState != REJECTED_STATE && rule.nextState != ACCEPTED_STATE }
                    .map { (idx, rule) ->
                        val inversedPreceedingRules =
                            rawRules.subList(0, idx).map { it.inverse() }.map { Condition(it.key, it.op, it.value) }
                        val thisRule = listOf(Condition(rule.key, rule.op, rule.value))
                        CompositeCondition(inversedPreceedingRules + thisRule, rule.nextState)
                    })
            return nonAcceptedNorRejectedConditions
        }

        companion object {
            fun parse(input: String): Job {
                val curlyIndex = input.indexOf("{")
                val id = input.substring(0, curlyIndex).trim()
                val rawRules = input.substring(curlyIndex).drop(1).dropLast(1)
                val lastComma = rawRules.lastIndexOf(",")
                val endState = rawRules.substring(lastComma + 1).trim()
                val ops = mapOf("<" to { part: Part, value: Int, key: String -> part.ratings[key]!! < value },
                    ">" to { part: Part, value: Int, key: String -> part.ratings[key]!! > value })
                val rules = rawRules.substring(0, lastComma).split(",").map {
                    val pattern = """(\w+)([<>])(\d+):(\w+)""".toRegex()
                    val matchResult = pattern.matchEntire(it.trim())!!
                    val (key, op, value, nextState) = matchResult.destructured
                    Rule(key, op, value.toInt(), nextState)
                }

                val rulesAsFunctions =
                    rules.map { { part: Part -> ops[it.op]!!(part, it.value, it.key) } to it.nextState }

                return Job(id, rulesAsFunctions, endState, rules)
            }
        }
    }

    data class Workflow(val jobs: Map<String, Job>) {
        fun isAccepted(part: Part): Boolean {
            val startJob = jobs[START_STATE]!!
            var state = startJob.evaluate(part)
            while (state != ACCEPTED_STATE && state != REJECTED_STATE) {
                val job = jobs[state]!!
                state = job.evaluate(part)
            }
            return state == ACCEPTED_STATE
        }
    }

    fun part1(input: List<String>): String {
        val partitionIndex = input.withIndex().find { it.value.isEmpty() }!!.index
        val jobs = input.subList(0, partitionIndex).map { Job.parse(it) }
        val parts = input.subList(partitionIndex + 1, input.size).map { Part.parse(it) }
        val workflow = Workflow(jobs.associateBy { it.id })
        return parts.filter { workflow.isAccepted(it) }.sumOf { it.score() }.toString()
    }

    data class MinAndMax(val min: Long, val max: Long, val key: String) {
        fun coerce(other: MinAndMax): MinAndMax {
            return MinAndMax(min.coerceAtLeast(other.min), max.coerceAtMost(other.max), key)
        }

        fun length(): Long = max - min + 1
    }

    fun part2(input: List<String>): String {
        val partitionIndex = input.withIndex().find { it.value.isEmpty() }!!.index
        val jobs = input.subList(0, partitionIndex).map { Job.parse(it) }
        val rejectedPaths = PathFinder(jobs.associateBy { it.id }).findPathsToRejectedState()
        val minAndMaxes: List<Map<String, MinAndMax>> = rejectedPaths
            .filter { it.compositeConditions.isNotEmpty() }
            .map { path ->
                path.compositeConditions.flatMap { it.conditions }
                    .map { it.toMinAndMax() }.groupBy { it.key }
                    .mapValues { (_, minAndMaxes) -> minAndMaxes.reduce { a, b -> a.coerce(b) } }
            }
        val possibleRejectedStates = minAndMaxes.map { minAndMax ->
            listOf("x", "m", "a", "s").map { minAndMax[it] ?: MinAndMax(1, 4000, it) }
                .map { it.length() }
                .reduce { acc, num -> acc * num }
        }.sum()
        val possibleStates = 4000L * 4000L * 4000L * 4000L
        val delta = possibleStates - possibleRejectedStates
        return delta.toString()
    }
}
