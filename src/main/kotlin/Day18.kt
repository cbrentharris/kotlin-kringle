import java.util.*
import kotlin.String
import kotlin.collections.List

object Day18 {
    data class Segment(val coordinates: Set<Day17.Coordinate>) {
        val minX = coordinates.minOf { it.x }
        val maxX = coordinates.maxOf { it.x }
        val minY = coordinates.minOf { it.y }
        val maxY = coordinates.maxOf { it.y }

        fun crosses(coordinate: Day17.Coordinate): Boolean {
            return if (minX == maxX && minX == coordinate.x) {
                coordinate.y in minY..maxY
            } else if (minY == maxY && minY == coordinate.y) {
                coordinate.x in minX..maxX
            } else {
                false
            }
        }

        fun crosses(y: Int): Boolean {
            return y in minY..maxY
        }

        fun length(): Int {
            return if (minX == maxX) {
                maxY - minY + 1
            } else {
                maxX - minX + 1
            }
        }

        /**
         * Returns true if the segment is vertical, false otherwise
         */
        fun isVertical(): Boolean {
            return minX == maxX
        }

        /**
         * Returns true if the segment is horizontal, false otherwise
         */
        fun isHorizontal(): Boolean {
            return minY == maxY
        }

        override fun hashCode(): Int {
            return Objects.hash(minX, maxX, minY, maxY)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Segment

            if (coordinates != other.coordinates) return false
            if (minX != other.minX) return false
            if (maxX != other.maxX) return false
            if (minY != other.minY) return false
            if (maxY != other.maxY) return false

            return true
        }
    }

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    data class Instruction(val direction: Direction, val steps: Int, val colorCode: String) {

        fun toTrueInstruction(): Instruction {
            val directionCode = colorCode.last()
            val hexSteps = colorCode.drop(1).dropLast(1)
            val steps = hexSteps.toInt(16)
            val direction = when (directionCode) {
                '0' -> Direction.RIGHT
                '1' -> Direction.DOWN
                '2' -> Direction.LEFT
                '3' -> Direction.UP
                else -> throw IllegalStateException("Should not happen")
            }
            return Instruction(direction, steps, colorCode)
        }

        companion object {
            fun parse(input: String): Instruction {
                val pattern = "([U|L|D|R]) ([0-9]+) \\((#[a-z0-9]+)\\)".toRegex()
                val (rawDirection, rawSteps, rawColorCode) = pattern.matchEntire(input)!!.destructured

                val direction = when (rawDirection) {
                    "U" -> Direction.UP
                    "D" -> Direction.DOWN
                    "L" -> Direction.LEFT
                    "R" -> Direction.RIGHT
                    else -> throw IllegalStateException("Should not happen")
                }
                val steps = rawSteps.toInt()
                return Instruction(direction, steps, rawColorCode)
            }
        }
    }

    fun part1(input: List<String>): String {
        val instructions = input.map { Instruction.parse(it) }
        return cubicMetersEnclosed(instructions).toString()
    }

    fun cubicMetersEnclosed(instructions: List<Instruction>): Long {
        val segments = dig(instructions)

        val minY = segments.minOf { it.minY }
        val maxY = segments.maxOf { it.maxY }
        val minX = segments.minOf { it.minX }
        val maxX = segments.maxOf { it.maxX }
        val delta = maxY - minY
        var current = 0
        var mySegments = segments.sortedWith(compareBy({ it.minY }, { it.maxY }))
        val enclosedCoordinates = (minY..maxY).sumOf { y ->
            val startCoordinate = Day17.Coordinate(minX, y)
            val endCoordinate = Day17.Coordinate(maxX, y)
            val ret = rayCast(startCoordinate, endCoordinate, mySegments)
            current += 1
            if (current % 1000000 == 0) {
                mySegments = mySegments.filter { it.maxY >= y }
                println("Progress: ${String.format("%.2f", current.toDouble() / delta * 100)}%")
            }
            ret
        }

        val segmentSizes = segments.sumOf { it.length() }
        val duplicateSegments =
            segments.flatMap { it.coordinates.toList() }.groupBy { it }.filter { it.value.size > 1 }.keys.size
        return (enclosedCoordinates + segmentSizes - duplicateSegments)
    }

    private fun rayCast(
        startCoordinate: Day17.Coordinate,
        endCoordinate: Day17.Coordinate,
        segments: List<Segment>
    ): Long {
        var segmentsEncountered = 0
        var cellsWithinPolygon = 0L
        val runningSegmentsCrossed = mutableSetOf<Segment>()
        var currentX = startCoordinate.x
        val segmentsCrossedByRay = segments
            .takeWhile { it.minY <= endCoordinate.y }
            .filter { it.crosses(startCoordinate.y) }
        while (currentX <= endCoordinate.x) {
            val currentCoordinate = Day17.Coordinate(currentX, startCoordinate.y)
            val segmentCrosses = segmentsCrossedByRay.filter { it.crosses(currentCoordinate) }
            val sizeBefore = runningSegmentsCrossed.size
            runningSegmentsCrossed.addAll(segmentCrosses)
            val sizeAfter = runningSegmentsCrossed.size
            val touchedAnotherSegment = sizeAfter > sizeBefore

            if (segmentCrosses.isNotEmpty()) {
                // Advance the current position to the max x value of the segment we crossed
                currentX = segmentCrosses.maxOf { it.maxX }
                // This is an odd edge case
                // where there are overlapping segments that are connected
                // but we encountered the last one
                if (!touchedAnotherSegment) {
                    currentX += 1
                }
            } else {
                if (isSingleOrSnakeShaped(runningSegmentsCrossed)) {
                    segmentsEncountered += 1
                }
                runningSegmentsCrossed.clear()
                val nextSegmentToBeCrossed = segmentsCrossedByRay
                    .minOfOrNull { if (it.minX > currentX) it.minX else Int.MAX_VALUE }
                val nextX = nextSegmentToBeCrossed ?: (endCoordinate.x + 1)
                // If the number of segments encountered is odd, then we are within the polygon
                val isWithinPolygon = segmentsEncountered % 2 == 1
                if (isWithinPolygon) {
                    val lengthOfEmptySpace = nextX - currentX
                    cellsWithinPolygon += lengthOfEmptySpace
                }
                // Advance the current position to the next segment crossing since all of
                // these are filled or ignored
                currentX = nextX
            }
        }
        return cellsWithinPolygon
    }

    /**
     * An annoying case in which we can run into
     * is when we have a single segment or a snake shaped segment
     *
     * #
     * #
     * #
     * # # #
     *     #
     *     #
     *     #
     *
     * This counts as one segment
     */
    private fun isSingleOrSnakeShaped(crossedSegments: Set<Segment>): Boolean {
        if (crossedSegments.isEmpty()) {
            return false
        }
        if (crossedSegments.size == 1) {
            return true
        }
        if (crossedSegments.size == 2) {
            return false
        }

        val verticalSegments = crossedSegments.count { it.isVertical() }
        val horizontalSegments = crossedSegments.count { it.isHorizontal() }
        return if (verticalSegments > horizontalSegments) {
            // This means that the segments are vertical but share a same y coordinte, like
            //
            //  #
            //  #
            //  #
            //  # # #
            //      #
            //      #
            //      #
            val sorted = crossedSegments.sortedWith(
                compareBy({ it.minX }, { it.maxX })
            )
            val start = sorted.first()
            val end = sorted.last()
            val ys = start.coordinates.map { it.y }.toList() + end.coordinates.map { it.y }.toList()
            val grouped = ys.groupBy { it }
            val ysThatAppearTwice = grouped.filter { it.value.size == 2 }.keys.first()
            ysThatAppearTwice > ys.min() && ysThatAppearTwice < ys.max()
        } else {
            // This means that the segments are horizontal but share a same x coordinate, like
            //
            //  # # # #
            //  #
            //  #
            //  #
            //  #
            //  #
            //  # # # #
            val sorted = crossedSegments.sortedWith(compareBy({ it.minY }, { it.maxY }))
            val start = sorted.first()
            val end = sorted.last()
            val xs = start.coordinates.map { it.x }.toList() + end.coordinates.map { it.x }.toList()
            val grouped = xs.groupBy { it }
            val xsThatAppearTwice = grouped.filter { it.value.size == 2 }.keys.first()
            xsThatAppearTwice > xs.min() && xsThatAppearTwice < xs.max()
        }
    }

    /**
     * Take a list of instructions that take a starting point and a direction to "dig", which
     * will then be represented as line segments
     */
    fun dig(instructions: List<Instruction>): List<Segment> {
        val currentCoordinate = Day17.Coordinate(0, 0)
        return instructions.fold(currentCoordinate to emptyList<Segment>()) { (currentCoordinate, segments), instruction ->
            val direction = instruction.direction
            val steps = instruction.steps
            val nextCoordinate = when (direction) {
                Direction.UP -> Day17.Coordinate(currentCoordinate.x, currentCoordinate.y - steps)
                Direction.DOWN -> Day17.Coordinate(currentCoordinate.x, currentCoordinate.y + steps)
                Direction.LEFT -> Day17.Coordinate(currentCoordinate.x - steps, currentCoordinate.y)
                Direction.RIGHT -> Day17.Coordinate(currentCoordinate.x + steps, currentCoordinate.y)
            }
            val segment = Segment(setOf(currentCoordinate, nextCoordinate))
            nextCoordinate to (segments + listOf(segment))
        }.second
    }

    fun part2(input: List<String>): String {
        val instructions = input.map { Instruction.parse(it) }.map { it.toTrueInstruction() }
        return cubicMetersEnclosed(instructions).toString()
    }
}
