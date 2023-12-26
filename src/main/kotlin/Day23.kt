import kotlin.String
import kotlin.collections.List
import Day17.Coordinate
import java.util.*
import kotlin.math.max

object Day23 {

    /**
     * A more compact representation of a path than a list of coordinates.
     */
    data class Segment(val steps: Int, val start: Coordinate, val end: Coordinate)

    fun part1(input: List<String>): String {
        val startIndex = input[0].indexOf(".")
        val endIndex = input.last().indexOf(".")
        val startCoordinate = Coordinate(startIndex, 0)
        val endCoordinate = Coordinate(endIndex, input.size - 1)
        return maxSteps(startCoordinate, startCoordinate, endCoordinate, input, 0).toString()
    }

    data class StepInformation(
        val node: Segment,
        val visited: Set<Segment>,
        val steps: Int
    )

    private fun maxStepsExpanded(
        start: Segment,
        end: Segment,
        nodes: Set<Segment>,
        input: List<String>
    ): Int {
        val queue = PriorityQueue<StepInformation> { a, b -> -a.steps.compareTo(b.steps) }
        queue.add(StepInformation(start, setOf(start), start.steps))
        val startMap = nodes.associate { it.start to it }
        val endMap = nodes.associate { it.end to it }
        val nodeBeforeExit = getNeighbors(end.start, input).map { it.first }.filter { it in endMap }
            .map { endMap[it] }.filterNotNull().distinct().first()
        var max = 0
        var count = 0
        val startMemo = mutableMapOf<Segment, List<Segment>>()
        val endMemo = mutableMapOf<Segment, List<Segment>>()
        while (queue.isNotEmpty()) {
            count++
            if (count % 1000000 == 0) {
                println("Count: $count, queue size: ${queue.size}, current max: $max")
            }
            val stepInformation = queue.poll()
            val node = stepInformation.node
            val steps = stepInformation.steps
            val visited = stepInformation.visited
            if (node == end) {
                max = max(steps, max)
                continue
            }
            if (nodeBeforeExit in visited) {
                continue
            }

            val startNeighbors = startMemo.getOrPut(node) {
                getNeighbors(node.start, input).map { it.first }.filter { it in endMap }
                    .map { endMap[it]!! }
            }.filter { it !in visited }
            val endNeighbors = endMemo.getOrPut(node) {
                getNeighbors(node.end, input).map { it.first }.filter { it in startMap }
                    .map { startMap[it]!! }
            }.filter { it !in visited }
            queue.addAll(
                (startNeighbors + endNeighbors).distinct()
                    .map { StepInformation(it, visited + node, steps + it.steps) })
        }
        return max
    }

    private tailrec fun maxSteps(
        previousCoordinate: Coordinate,
        currentCoordinate: Coordinate,
        endCoordinate: Coordinate,
        input: List<String>,
        currentSteps: Int
    ): Int {
        if (currentCoordinate == endCoordinate) {
            return currentSteps
        }

        val nextCoordinates = getNeighborsDirectionally(previousCoordinate, currentCoordinate, input)
        val nextOpenSpace = nextCoordinates.find { it.second == '.' }
        if (nextOpenSpace != null) {
            return maxSteps(
                currentCoordinate,
                nextOpenSpace.first,
                endCoordinate,
                input,
                currentSteps + 1
            )
        }

        return nextCoordinates.maxOf { maxSteps(currentCoordinate, it.first, endCoordinate, input, currentSteps + 1) }
    }

    fun part2(input: List<String>): String {
        val startIndex = input[0].indexOf(".")
        val endIndex = input.last().indexOf(".")
        val startCoordinate = Coordinate(startIndex, 0)
        val endCoordinate = Coordinate(endIndex, input.size - 1)
        val nodes = buildSegments(startCoordinate, endCoordinate, input, 0)
        val start = nodes.find { it.start == startCoordinate }
        return maxStepsExpanded(start!!, nodes.find { it.end == endCoordinate }!!, nodes, input).toString()
    }

    data class SegmentBuildingInformation(
        val coordinate: Coordinate,
        val previousCoordinate: Coordinate,
        val steps: Int,
        val runningSegment: Segment
    )

    private fun buildSegments(
        startCoordinate: Coordinate,
        endCoordinate: Coordinate,
        input: List<String>,
        length: Int
    ): Set<Segment> {
        val queue = ArrayDeque<SegmentBuildingInformation>()
        queue.add(
            SegmentBuildingInformation(
                startCoordinate,
                startCoordinate,
                length,
                Segment(length, startCoordinate, startCoordinate)
            )
        )
        val segments = mutableSetOf<Segment>()
        while (queue.isNotEmpty()) {
            val stepInformation = queue.poll()
            val currentCoordinate = stepInformation.coordinate
            val previousCoordinate = stepInformation.previousCoordinate
            val length = stepInformation.steps
            val runningNode = stepInformation.runningSegment
            if (currentCoordinate == endCoordinate) {
                val updatedNode = runningNode.copy(end = endCoordinate, steps = length)
                if (updatedNode !in segments) {
                    segments.add(updatedNode)
                }
            }
            val nextCoordinates = getNeighborsDirectionally(previousCoordinate, currentCoordinate, input)

            val nextOpenSpace = nextCoordinates.find { it.second == '.' }
            if (nextOpenSpace != null) {
                val currentCharacter = input[currentCoordinate.y][currentCoordinate.x]
                // We have to treat >, < etc as their own segment because otherwise we can lead to duplicate counting
                if (currentCharacter != '.') {
                    val updatedNode = runningNode.copy(end = currentCoordinate, steps = length)
                    if (updatedNode !in segments) {
                        segments.add(updatedNode)
                    }
                    queue.addFirst(
                        SegmentBuildingInformation(
                            nextOpenSpace.first,
                            currentCoordinate,
                            1,
                            Segment(1, nextOpenSpace.first, nextOpenSpace.first)
                        )
                    )
                } else {
                    queue.addFirst(SegmentBuildingInformation(nextOpenSpace.first, currentCoordinate, length + 1, runningNode))
                }
            } else {
                val updatedNode = runningNode.copy(end = currentCoordinate, steps = length)
                if (updatedNode !in segments) {
                    segments.add(updatedNode)
                }
                val neighbors = nextCoordinates.map {
                    SegmentBuildingInformation(
                        it.first,
                        currentCoordinate,
                        1,
                        Segment(1, it.first, it.first)
                    )
                }
                for (nextNeighbor in neighbors) {
                    queue.addFirst(nextNeighbor)
                }
            }
        }
        return segments
    }

    fun getNeighborsDirectionally(
        previousCoordinate: Coordinate,
        currentCoordinate: Coordinate,
        input: List<String>
    ): List<Pair<Coordinate, Char>> {
        val left = currentCoordinate.copy(x = currentCoordinate.x - 1)
        val right = currentCoordinate.copy(x = currentCoordinate.x + 1)
        val up = currentCoordinate.copy(y = currentCoordinate.y - 1)
        val down = currentCoordinate.copy(y = currentCoordinate.y + 1)
        val nextCoordinates = getNeighbors(currentCoordinate, input)
            .filter { it.first != previousCoordinate }
            .filter {
                if (it.first == left) {
                    it.second != '>'
                } else if (it.first == right) {
                    it.second != '<'
                } else if (it.first == up) {
                    it.second != 'v'
                } else if (it.first == down) {
                    it.second != '^'
                } else {
                    throw Exception("Unknown direction")
                }
            }
        return nextCoordinates
    }

    fun getNeighbors(currentCoordinate: Coordinate, input: List<String>): List<Pair<Coordinate, Char>> {
        val left = currentCoordinate.copy(x = currentCoordinate.x - 1)
        val right = currentCoordinate.copy(x = currentCoordinate.x + 1)
        val up = currentCoordinate.copy(y = currentCoordinate.y - 1)
        val down = currentCoordinate.copy(y = currentCoordinate.y + 1)
        val nextCoordinates = listOf(
            left,
            right,
            up,
            down
        ).filter { it.x >= 0 && it.x < input[0].length && it.y >= 0 && it.y < input.size }
            .map { it to input[it.y][it.x] }
            .filter { it.second != '#' }
        return nextCoordinates
    }
}
