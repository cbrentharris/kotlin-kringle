import kotlin.String
import kotlin.collections.List
import kotlin.math.*

object Day6 {
    fun part1(input: List<String>): String {
        val parseNumbers: (String, String) -> List<Int> = { str: String, prefix: String ->
            str.replace(prefix, "")
                .trim()
                .split("\\s+".toRegex())
                .map { it.toInt() }
        }
        val times = parseNumbers(input.first(), "Time:")
        val distances = parseNumbers(input.last(), "Distance:")

        return times.zip(distances)
            .map { (time, distance) ->
                val (maxTime, minTime) = maxAndMinTimes(time.toLong(), distance.toLong())
                maxTime - minTime + 1
            }.reduce { a, b -> a * b }.toString()
    }

    fun part2(input: List<String>): String {
        val parseNumber: (String, String) -> Long = { str: String, prefix: String ->
            str.replace(prefix, "")
                .trim()
                .replace(" ", "")
                .toLong()
        }
        val time = parseNumber(input.first(), "Time:")
        val distance = parseNumber(input.last(), "Distance:")
        val (maxTime, minTime) = maxAndMinTimes(time, distance)
        // Add 1 to count the upper as inclusive
        return (maxTime - minTime + 1).toString()
    }

    /**
     * If we look at the equation for distance, we get:
     * distance = holdTime * (time - holdTime)
     * Turning this into a quadratic equation, we get:
     * 0 = -holdTime^2 + time * holdTime - distance
     *
     * We can then use the quadratic formula to find the holdTime:
     * holdTime = (-time +- sqrt(time^2 - 4 * -(distance + 1))) / -2
     *
     * Note, we set distance + 1 because we have to exceed the target distance
     */
    private fun maxAndMinTimes(time: Long, distance: Long): Pair<Long, Long> {
        val squareRoot = sqrt(time.toDouble().pow(2) - 4 * (distance + 1))
        val upper = (-time - squareRoot) / -2
        val lower = (-time + squareRoot) / -2
        // We have to floor the upper because we cannot hold fractional seconds, and have to ceil the lower
        return floor(upper).toLong() to ceil(lower).toLong()
    }
}
