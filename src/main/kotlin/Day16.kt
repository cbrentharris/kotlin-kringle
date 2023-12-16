import kotlin.String
import kotlin.collections.List

public object Day16 {
    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public fun part1(input: List<String>): String {
        val startCoordinate = Day10.Coordinate(0, 0)
        val startDirection = Direction.RIGHT
        return energizeCount(startCoordinate, startDirection, input).toString()
    }

    private fun energizeCount(startCoordinate: Day10.Coordinate, startDirection: Direction, grid: List<String>): Int {
        val seenCoordinates = mutableSetOf<Pair<Day10.Coordinate, Direction>>()
        val queue = ArrayDeque<Pair<Day10.Coordinate, Direction>>()
        queue.add(startCoordinate to startDirection)
        while (queue.isNotEmpty()) {
            val (currentCoordinate, currentDirection) = queue.removeFirst()
            if (currentCoordinate.x < 0 || currentCoordinate.y < 0 || currentCoordinate.y >= grid.size || currentCoordinate.x >= grid[currentCoordinate.y].length) {
                continue
            }
            if (seenCoordinates.contains(currentCoordinate to currentDirection)) {
                continue
            }
            seenCoordinates.add(currentCoordinate to currentDirection)
            val nextCoordinates = next(currentCoordinate, currentDirection, grid)
            queue.addAll(nextCoordinates)
        }
        return seenCoordinates.map { (coordinate, _) -> coordinate }.distinct().count()
    }

    private fun next(
        currentCoordinate: Day10.Coordinate,
        direction: Direction,
        grid: List<String>
    ): List<Pair<Day10.Coordinate, Direction>> {
        val currentMarker = grid[currentCoordinate.y][currentCoordinate.x]
        when (currentMarker) {
            '|' -> {
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    val nextCoordinate = when (direction) {
                        Direction.UP -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y - 1)
                        Direction.DOWN -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y + 1)
                        else -> throw IllegalStateException("Should not happen")
                    }
                    // Keep going the same direction
                    return listOf(nextCoordinate to direction)
                } else {
                    return listOf(
                        Day10.Coordinate(currentCoordinate.x, currentCoordinate.y - 1) to Direction.UP,
                        Day10.Coordinate(currentCoordinate.x, currentCoordinate.y + 1) to Direction.DOWN
                    )
                }
            }

            '-' -> {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    val nextCoordinate = when (direction) {
                        Direction.LEFT -> Day10.Coordinate(currentCoordinate.x - 1, currentCoordinate.y)
                        Direction.RIGHT -> Day10.Coordinate(currentCoordinate.x + 1, currentCoordinate.y)
                        else -> throw IllegalStateException("Should not happen")
                    }
                    // Keep going the same direction
                    return listOf(nextCoordinate to direction)
                } else {
                    return listOf(
                        Day10.Coordinate(currentCoordinate.x - 1, currentCoordinate.y) to Direction.LEFT,
                        Day10.Coordinate(currentCoordinate.x + 1, currentCoordinate.y) to Direction.RIGHT
                    )
                }
            }

            '\\' -> {
                val (nextCoordinate, nextDirection) = when (direction) {
                    Direction.RIGHT -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y + 1) to Direction.DOWN
                    Direction.LEFT -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y - 1) to Direction.UP
                    Direction.UP -> Day10.Coordinate(currentCoordinate.x - 1, currentCoordinate.y) to Direction.LEFT
                    Direction.DOWN -> Day10.Coordinate(currentCoordinate.x + 1, currentCoordinate.y) to Direction.RIGHT
                }
                return listOf(nextCoordinate to nextDirection)
            }

            '/' -> {
                val (nextCoordinate, nextDirection) = when (direction) {
                    Direction.LEFT -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y + 1) to Direction.DOWN
                    Direction.RIGHT -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y - 1) to Direction.UP
                    Direction.DOWN -> Day10.Coordinate(currentCoordinate.x - 1, currentCoordinate.y) to Direction.LEFT
                    Direction.UP -> Day10.Coordinate(currentCoordinate.x + 1, currentCoordinate.y) to Direction.RIGHT
                }
                return listOf(nextCoordinate to nextDirection)
            }

            '.' -> {
                val nextCoordinate = when (direction) {
                    Direction.UP -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y - 1)
                    Direction.DOWN -> Day10.Coordinate(currentCoordinate.x, currentCoordinate.y + 1)
                    Direction.LEFT -> Day10.Coordinate(currentCoordinate.x - 1, currentCoordinate.y)
                    Direction.RIGHT -> Day10.Coordinate(currentCoordinate.x + 1, currentCoordinate.y)
                }
                return listOf(nextCoordinate to direction)
            }

            else -> throw IllegalStateException("Should not happen")
        }
    }

    public fun part2(input: List<String>): String {
        val leftStarts = (0 until input.size).map { y ->
            Day10.Coordinate(0, y) to Direction.RIGHT
        }

        val rightStarts = (0 until input.size).map { y ->
            Day10.Coordinate(input[y].length - 1, y) to Direction.LEFT
        }
        val topStarts = (0 until input[0].length).map { x ->
            Day10.Coordinate(x, 0) to Direction.DOWN
        }
        val bottomStarts = (0 until input[0].length).map { x ->
            Day10.Coordinate(x, input.size - 1) to Direction.UP
        }
        val allStarts = leftStarts + rightStarts + topStarts + bottomStarts
        return allStarts.maxOf { (coordinate, direction) ->
            energizeCount(coordinate, direction, input)
        }.toString()
    }
}
