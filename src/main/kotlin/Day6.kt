import kotlin.String
import kotlin.collections.List
import kotlin.math.*

object Day6 {
    fun part1(input: List<String>): String {
        val times = input.first().replace("Time:", "")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toInt() }
        val distances = input.last().replace("Distance:", "")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toInt() }

        return times.zip(distances)
            .map { (time, distance) ->
                val (maxTime, minTime) = minAndMaxTimes(time.toLong(), distance.toLong())
                maxTime - minTime + 1
            }.reduce { a, b -> a * b }.toString()
    }

    fun part2(input: List<String>): String {
        val time = input.first().replace("Time:", "")
            .trim()
            .replace(" ", "")
            .toLong()
        val distance = input.last().replace("Distance:", "")
            .trim()
            .replace(" ", "")
            .toLong()
        val (maxTime, minTime) = minAndMaxTimes(time, distance)
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
    private fun minAndMaxTimes(time: Long, distance: Long): Pair<Long, Long> {
        val upper = (-time - sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / -2
        val lower = (-time + sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / -2
        // We have to floor the upper because we cannot hold fractional seconds, and have to ceil the lower
        return floor(upper).toLong() to ceil(lower).toLong()
    }
}
