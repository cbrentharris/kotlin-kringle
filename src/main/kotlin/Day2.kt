import kotlin.String
import kotlin.collections.List

object Day2 {
    data class Cube(
        val color: String,
        val count: Int
    )

    data class Game(
        val id: Int,
        val cubePulls: List<List<Cube>>
    ) {
        companion object {
            fun parseGame(s: String): Game {
                val (id, pulls) = s.split(":")
                return Game(
                    id.trimStart(*"Game ".toCharArray()).toInt(),
                    pulls.split(";").map { cubeList ->
                        cubeList.split(",").map { cube ->
                            val (count, color) = cube.trimStart(' ').split(" ")
                            Cube(color, count.toInt())
                        }
                    }
                )
            }
        }
    }

    fun part1(input: List<String>): String {
        return sumOfGamesWithTarget(
            input.map(Game::parseGame),
            listOf(
                Cube("red", 12), Cube("green", 13), Cube("blue", 14)
            )
        ).toString()
    }

    private fun sumOfGamesWithTarget(input: List<Game>, targets: List<Cube>): Int {
        val index = targets.associateBy { it.color }
        return input.filter {
            it.cubePulls.all { cubes ->
                cubes.all { cube ->
                    cube.count <= index.getOrDefault(
                        cube.color,
                        cube
                    ).count
                }
            }
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): String {
        return sumOfPowersWithMinimums(input.map(Game::parseGame)).toString()
    }

    private fun sumOfPowersWithMinimums(games: List<Game>): Int {
        return games.map {
            val minimumCubesNeeded = it.cubePulls.flatten()
                .groupBy { it.color }
                .mapValues { it.value.maxBy { it.count } }
            minimumCubesNeeded.map { it.value!!.count }.reduce { a, b -> a * b }
        }.sum()
    }
}
