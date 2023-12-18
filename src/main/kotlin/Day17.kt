import java.util.*
import kotlin.String
import kotlin.collections.List
import kotlin.math.min

object Day17 {
    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    data class CurrentStepInformation(
        val currentCoordinate: Coordinate,
        val currentDirection: Direction,
        val consecutiveDirectionSteps: Int,
        val heatLoss: Int
    ) {
    }

    data class Coordinate(val x: Int, val y: Int)

    fun part1(input: List<String>): String {
        val endCoordinate = Coordinate(input[0].length - 1, input.size - 1)
        return minimumHeatLoss(input, endCoordinate, maxConsecutiveSteps = 3).toString()
    }

    private fun minimumHeatLoss(
        grid: List<String>,
        endCoordinate: Coordinate,
        maxConsecutiveSteps: Int,
        minConsecutiveSteps: Int = 0
    ): Int {
        val heap = PriorityQueue<CurrentStepInformation> { o1, o2 ->
            o1.heatLoss.compareTo(o2.heatLoss)
        }
        heap.add(
            CurrentStepInformation(
                Coordinate(1, 0),
                Direction.RIGHT,
                1,
                0
            )
        )
        heap.add(
            CurrentStepInformation(
                Coordinate(0, 1),
                Direction.DOWN,
                1,
                0
            )
        )
        var minValue = Int.MAX_VALUE
        val memo = mutableMapOf<CurrentStepInformation, Int>()
        while (heap.isNotEmpty()) {
            val currentStepInformation = heap.poll()
            val currentCoordinate = currentStepInformation.currentCoordinate
            val heatLoss =
                grid[currentCoordinate.y][currentCoordinate.x].digitToInt()
            val memoKey = currentStepInformation.copy(heatLoss = 0)
            // If we have already been here with less heat loss, we can skip this
            // Especially if we have already been here with the same heat loss but less consecutive steps, because
            // if a path had more possibilites and a smaller heat loss it makes no sense to search the space
            val tooExpensive =
                (minConsecutiveSteps..memoKey.consecutiveDirectionSteps - 1).map {
                    memoKey.copy(
                        consecutiveDirectionSteps = it
                    )
                }
                    .any {
                        memo.containsKey(it) && memo[it]!! <= currentStepInformation.heatLoss + heatLoss
                    } || (memo[memoKey] ?: Int.MAX_VALUE) <= currentStepInformation.heatLoss + heatLoss
            if (tooExpensive) {
                continue
            }

            memo.put(memoKey, currentStepInformation.heatLoss + heatLoss)

            if (currentCoordinate == endCoordinate) {
                if (currentStepInformation.consecutiveDirectionSteps < minConsecutiveSteps) {
                    continue
                }
                minValue = min(minValue, currentStepInformation.heatLoss + heatLoss)
                continue
            }

            val nextCoordinates = listOf(
                currentCoordinate.copy(x = currentCoordinate.x + 1) to Direction.RIGHT,
                currentCoordinate.copy(y = currentCoordinate.y + 1) to Direction.DOWN,
                currentCoordinate.copy(x = currentCoordinate.x - 1) to Direction.LEFT,
                currentCoordinate.copy(y = currentCoordinate.y - 1) to Direction.UP
            ).filter { (_, direction) ->
                // We cannot reverse directions
                when (direction) {
                    Direction.UP -> currentStepInformation.currentDirection != Direction.DOWN
                    Direction.DOWN -> currentStepInformation.currentDirection != Direction.UP
                    Direction.LEFT -> currentStepInformation.currentDirection != Direction.RIGHT
                    Direction.RIGHT -> currentStepInformation.currentDirection != Direction.LEFT
                }
            }.map { (coordinate, direction) ->
                CurrentStepInformation(
                    coordinate,
                    direction,
                    if (direction == currentStepInformation.currentDirection) currentStepInformation.consecutiveDirectionSteps + 1 else 1,
                    currentStepInformation.heatLoss + heatLoss
                )
            }.filter {
                // We must move a minimum number of steps in the same direction
                it.currentDirection == currentStepInformation.currentDirection || currentStepInformation.consecutiveDirectionSteps >= minConsecutiveSteps
            }.filter {
                // We cannot move in the same direction more than maxConsecutiveSteps
                it.consecutiveDirectionSteps <= maxConsecutiveSteps
            }.filter {
                // We cannot move out of bounds
                it.currentCoordinate.x >= 0 && it.currentCoordinate.y >= 0 && it.currentCoordinate.y < grid.size && it.currentCoordinate.x < grid[it.currentCoordinate.y].length
            }
            heap.addAll(nextCoordinates)
        }
        return minValue
    }

    fun part2(input: List<String>): String {
        val endCoordinate = Coordinate(input[0].length - 1, input.size - 1)
        return minimumHeatLoss(
            input, endCoordinate, maxConsecutiveSteps = 10, minConsecutiveSteps = 4
        ).toString()
    }
}
