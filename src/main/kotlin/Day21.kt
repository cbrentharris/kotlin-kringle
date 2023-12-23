import kotlin.String
import kotlin.collections.List
import kotlin.math.pow

object Day21 {
    fun part1(input: List<String>, targetSteps: Long): String {
        val gardenPlots = input.withIndex().map { (y, row) ->
            row.withIndex().map { (x, char) ->
                // Rock is #, plot is .
                if (char != '#') {
                    Day17.Coordinate(x, y)
                } else {
                    null
                }
            }
        }.flatten().filterNotNull().toSet()
        val source = input.find { 'S' in it }!!.let { Day17.Coordinate(it.indexOf('S'), input.indexOf(it)) }
        return (0 until targetSteps).fold(setOf(source)) { reachableGardenPlots, _ ->
            reachableGardenPlots.flatMap { nextCoordinates(it, gardenPlots) }.toSet()
        }.size.toString()
    }

    data class GridLifeCycle(val grid: Day17.Coordinate, val start: Long, val end: Long)

    /**
     *
     * lets say we have 9 grids
     *   GRID15 GRID7 GRID3 GRID8 GRID21
     *   GRID14 GRID4 GRID1 GRID2 GRID22
     *   GRID13 GRID9 GRID5  GRID6 GRID23
     *   GRID12 GRID10 GRID11 GRID16 GRID24
     *   GRID17 GRID18 GRID19 GRID20 GRID25
     *
     *
     *    so to start
     *
     *
     * 13 +25 = 38
     *
     *              * (1)
     *
     *              then
     *
     *               *
     *             * * * (5)
     *               *
     *
     *               Then
     *
     *               *
     *             * * *
     *           * * * * *  (13)
     *             * * *
     *               *
     *
     *               THen
     *
     *
     *               *
     *             * * *
     *           * * * * *
     *         * * * * * * * (1 + 3 + 5 + 7 + 5 + 3 + 1 = 25)
     *           * * * * *
     *             * * *
     *               *
     *               1
     *
     *                     (0,1)
     *              (-1,0) (0,0) (1,0) second diamond
     *                     (0,-1)
     *                      4
     *
     *                     1 even, 4 odd
     *
     *                     (0,2)
     *              (-1,1) (0,1) (1,1)
     *       (-2,0) (-1,0) (0,0) (1,0) (2, 0) -- third diamond -- ads 4 corners
     *              (-1,1) (0,-1) (1,-1)
     *                     (0,-2)
     *
     *                     8
     *
     * 4 odd, 9 event
     *
     *                     (0,3)
     *              (-1,2) (0,2) (1,2)
     *       (-2,1) (-1,1) (1,1) (1,1) (2,1)
     *(-3,0) (-2,0) (-1,0) (0,0) (1,0) (2,0) (3,0)
     *      (-2,-1) (-1,-1) (0,-1) (1,-1) (2,-1)
     *             (-1,-2) (0,-2) (1,-2)
     *                     (0,-3)
     *
     *                     9 even, 16 odd
     *
     *                     12 odd on the exterior, so 18 odd 5 event
     *
     *                              (0, 4)
     *                              (0, 3) (1, 3)
     *                              (0, 2) (1, 2) (2, 2)
     *                              (0, 1) (1, 1) (2, 1) (3, 1)
     *(-4,0) (-3,0), (-2,0), (-1,0) (0, 0) (1, 0) (2, 0) (3, 0) (4, 0)
     *                              (0, -1)
     *                              (0, -2)
     *                              (0, -3)
     *                              (0, -4)
     *
     *                     so
     *
     *               THen
     *
     *               1+3+5+7+9+11 = 36
     *
     *               *
     *             * * *
     *           * * * * *
     *         * * * * * * *
     *       * * * * * * * * * (1 + 3 + 5 + 7 + 9 + 7 + 5 + 3 + 1 = 49)
     *         * * * * * * *
     *           * * * * *
     *             * * *
     *               *
     *
     *               *
     *
     *               arithmetic progression = sn = n/2(2a + (n-1)d)
     *
     *
     *               a_{n}=a_{1}+(n-1)d
     *
     *
     *   so grid start is 5
     *
     *   then we go 9,1,6,11
     *
     *   9 could go 13, 5, 10, 4, right?
     *
     *   but it already hit 5, so we can ignore that, so 3?
     *
     *   11 could go 10, 16, 19, 5, but alrdy went 5 so can ignore that, and 11 shares a corner with 9 so we can ignore that, so 2
     *
     *   6 could go 5, 16, 23, 2 but we already went 5 and 16 so we can ignore that so 2?
     *
     *   1 could go 3, 4, 5, 2, but we already went 5 and 2 and 4 so we can ignore that, so we can go 1?
     *
     *   so we have the winner, that goes 3
     *
     *   then we have the middle losers and they can each go 2
     *
     *   and then we have th eloser, and he goes 1
     *
     *
     *
     *   if we get to grid 4 first before grid 5, then we can surmise that grid4 will reach grid 9 before grid 5 reaches grid 9
     *
     *   so in a 3x, 1 has to spawn 2, 3, 4, 5
     *   but 2,3,4,5 cant each spawn 9 7 8 6
     *
     *   so instead of 1 -> 4 -> 16
     *   its 1 -> 4 ->
     */
    fun part2(input: List<String>, targetSteps: Long): String {
        // Part two algorithm -- identify the minimum steps needed to cover all steps in the grid, starting from each position on the exterior of the grid
        // and the start position. Then add the grid spaces for the current position, and for each position on the exterior
        // start a new simulation placing the start at those positions and the target at the current position.
        val requiredProgressionNum = 4L
        var gardenPlots = input.withIndex().map { (y, row) ->
            row.withIndex().map { (x, char) ->
                if (char != '#') {
                    Day17.Coordinate(x, y)
                } else {
                    null
                }
            }
        }.flatten().filterNotNull().toSet()
        val newCoords = gardenPlots.filter { nextCoordinatesExpanded(it, gardenPlots, input).isNotEmpty() }
        gardenPlots = newCoords.toSet()
        val source = input.find { 'S' in it }!!.let { Day17.Coordinate(it.indexOf('S'), input.indexOf(it)) }
        var steps = 0L
        var reachableGardenPlots = setOf(source)
        // This map contains the number of garden plots reachable within a grid, based on the number of steps taken
        // modulo 2. This is because the number of garden plots reachable within a grid oscillates between
        // two numbers when even and odd.
        val plotsReachableWithinAGridBasedOnStepCount = mutableMapOf<Day17.Coordinate, MutableMap<Boolean, Long>>()
        val gardenPlotsProcessedPerGrid =
            mutableMapOf<Day17.Coordinate, MutableSet<Day17.Coordinate>>(Day17.Coordinate(0, 0) to mutableSetOf())
        val completedGrids = mutableSetOf<Day17.Coordinate>()
        val gridLifeCycle = mutableMapOf<Day17.Coordinate, GridLifeCycle>()
        var completedProgressionNum = 1L
        val gridRunningTotalsPerStep = mutableMapOf<Day17.Coordinate, MutableMap<Long, Long>>()
        val nextMemo = mutableMapOf<Day17.Coordinate, List<Day17.Coordinate>>()
        while (steps <= targetSteps) {
            if (steps % 100 == 0L) {
                println("Step $steps, reachable garden plots: ${reachableGardenPlots.size}")
            }
            if (completedGrids.size.toLong() == diamondSize(completedProgressionNum)) {
                if (completedProgressionNum == requiredProgressionNum) {
                    break
                }
                completedProgressionNum++
            }
            val gardenPlotsWithinOneStep =
                reachableGardenPlots.fold(mutableSetOf<Day17.Coordinate>()) { acc, gardenPlotCoordinate ->
                    val grid =
                        Day17.Coordinate(
                            gardenPlotCoordinate.x.floorDiv(input[0].length),
                            gardenPlotCoordinate.y.floorDiv(input.size)
                        )
                    if (grid in completedGrids) {
                        acc
                    } else {
                        if (grid !in gridLifeCycle) {
                            gridLifeCycle[grid] = GridLifeCycle(grid, steps + 1, 0)
                        }
                        val gardenPlotsProcessed = gardenPlotsProcessedPerGrid.getOrPut(grid) { mutableSetOf() }
                        val plotsReachableBasedOnStepCount = plotsReachableWithinAGridBasedOnStepCount.getOrPut(grid) {
                            mutableMapOf(
                                true to 0,
                                false to 0
                            )
                        }
                        val gridSpecificMap = gridRunningTotalsPerStep.getOrPut(grid) { mutableMapOf() }
                        gridSpecificMap[steps + 1] = gridSpecificMap.getOrDefault(steps + 1, 0) + 1
                        val haveProcessedAllPlotsWithinAGrid =
                            gardenPlotsProcessedPerGrid[grid]?.size == gardenPlots.size
                        if (haveProcessedAllPlotsWithinAGrid) {
                            completedGrids.add(grid)
                            gridLifeCycle[grid] = gridLifeCycle[grid]!!.copy(end = steps)
                            gridSpecificMap.remove(steps + 1)
                            gardenPlotsProcessedPerGrid[grid]!!.forEach { nextMemo.remove(it) }
                            gardenPlotsProcessedPerGrid.remove(grid)
                        }
                        val stepCountModuloTwo = steps % 2 == 0L
                        if (gardenPlotCoordinate !in gardenPlotsProcessed) {
                            gardenPlotsProcessed.add(gardenPlotCoordinate)
                            plotsReachableBasedOnStepCount[stepCountModuloTwo] =
                                plotsReachableBasedOnStepCount.getOrDefault(stepCountModuloTwo, 0) + 1
                        }
                        val next = nextMemo.getOrPut(gardenPlotCoordinate) {
                            nextCoordinatesExpanded(
                                gardenPlotCoordinate,
                                gardenPlots,
                                input
                            )
                        }
                        acc.addAll(next)
                        acc
                    }
                }
            reachableGardenPlots = gardenPlotsWithinOneStep
            steps++
        }
        val targetStepsModuloTwo = targetSteps % 2 == 0L
        if (completedProgressionNum < requiredProgressionNum) {
            return plotsReachableWithinAGridBasedOnStepCount.toList().sumOf { it.second[targetStepsModuloTwo] ?: 0 }
                .toString()
        }

        val repeatLength = input.size.toLong()

//        printGridAsDiamond(gridCounts.keys)

        // Using the grid lifecycle, we can determine the number of grids that are completed at each step
        // and the number of grids that are started at each step.
        val gridCoordinatesFromFirstTwoDiamonds = gridLifeCycle.toList().take(5).map { it.first }
        val stepCountFromFirstTwoDiamonds =
            gridCoordinatesFromFirstTwoDiamonds.sumOf {
                plotsReachableWithinAGridBasedOnStepCount[it]?.get(
                    targetStepsModuloTwo
                ) ?: 0
            }

        val stepsFromAllFutureDiamonds = gridLifeCycle.toList().drop(5).take(8)
            .map {
                stepsFromEachGrid(
                    it.first,
                    it.second,
                    targetSteps,
                    repeatLength,
                    plotsReachableWithinAGridBasedOnStepCount[it.first]!!,
                    gridRunningTotalsPerStep
                )
            }
        return (stepsFromAllFutureDiamonds.sumOf { it.second } + stepCountFromFirstTwoDiamonds).toString()
    }

    /**
     * This function calculates the number of steps taken to reach the target step, and the number of plots reachable
     * from the grid at that step.
     *
     * It has conditional logic based on if the initial grid was an edge (x or y == 0) or not.
     *
     * Edges will only ever produce one grid, and non edges can produce multiple grids.
     */
    private fun stepsFromEachGrid(
        grid: Day17.Coordinate,
        lifecycle: GridLifeCycle,
        targetSteps: Long,
        repeatLength: Long,
        plotsReachableBasedOnStepCount: Map<Boolean, Long> = emptyMap(),
        plotsReachablePerStepCount: Map<Day17.Coordinate, Map<Long, Long>> = emptyMap()
    ): Pair<Day17.Coordinate, Long> {
        val whenFirstGridIsReached = lifecycle.start
        val gridLife = lifecycle.end - lifecycle.start + 1
        val numberOfGridsReachable = (targetSteps + 1 - whenFirstGridIsReached) / repeatLength + 1
        val numberOfCoordinatesForkedFromNonEdges = (numberOfGridsReachable) * (numberOfGridsReachable + 1) / 2
        // We know that the grid lifecycle is longer than the steps between each grid is spawned, so we can use that to
        // ascertain that there will be two grids that potentially didin't finish
        val numberOfCompletedGrids = (targetSteps + 1 - whenFirstGridIsReached) / repeatLength - 1
        // When we are not an edge, we fork off multiple grids within a diamond
        val numberOfCompletedGridsForkedFromNonEdges = (numberOfCompletedGrids) * (numberOfCompletedGrids + 1) / 2

        val isEdge = grid.x == 0 || grid.y == 0
        val firstBatchStarts = whenFirstGridIsReached + repeatLength * (numberOfGridsReachable - 2)

        // For edges, the pattern isn't correct until the 3rd edge is reached
        val coordinateWhenPatternDetected = when (isEdge) {
            true -> when (grid.x == 0) {
                true -> when (grid.y < 0) {
                    true -> Day17.Coordinate(0, -3)
                    false -> Day17.Coordinate(0, 3)
                }

                false -> when (grid.x < 0) {
                    true -> Day17.Coordinate(-3, 0)
                    // 3,0 = 0,2
                    false -> Day17.Coordinate(3, 0)
                }
            }

            false -> grid
        }

        val plotsReachableByStepCount = plotsReachablePerStepCount[coordinateWhenPatternDetected]!!

        val gridsCompleted = if (isEdge) numberOfCompletedGrids else numberOfCompletedGridsForkedFromNonEdges
        val gridsStarted = if (isEdge) numberOfGridsReachable else numberOfCoordinatesForkedFromNonEdges


        val evenTargetSteps = targetSteps % 2 == 0L
        val oddTargetSteps = targetSteps % 2 == 1L

        val firstBatchCompletedSteps = if (isEdge) {
            // For edges, the next edge has a flip-flopped count
            if (gridsCompleted % 2 == 0L) {
                plotsReachableBasedOnStepCount[evenTargetSteps] ?: 0
            } else {
                plotsReachableBasedOnStepCount[oddTargetSteps] ?: 0
            }
        } else {
            plotsReachableBasedOnStepCount[evenTargetSteps] ?: 0
        }

        val coordinatesRemaining = gridsStarted - gridsCompleted
        val firstBatch = if (isEdge) (if (coordinatesRemaining > 1) 1 else 0) else (coordinatesRemaining - 1) / 2
        val secondBatch = if (isEdge) (if (coordinatesRemaining > 0) 1 else 0) else (coordinatesRemaining - 1) / 2 + 1
        val secondBatchStarts = whenFirstGridIsReached + repeatLength * (numberOfGridsReachable - 1)
        val firstBatchStepsTaken = targetSteps - firstBatchStarts + 1
        val secondBatchStepsTaken = targetSteps - secondBatchStarts + 1
        val mapOffset = plotsReachableByStepCount.minOf { it.key }
        val firstBatchStepsReachable = when (firstBatchStepsTaken < gridLife) {
            true -> plotsReachableByStepCount[firstBatchStepsTaken + mapOffset]!!
            false -> firstBatchCompletedSteps
        }
        val firstBatchValue =
            firstBatch * firstBatchStepsReachable
        val secondBatchValue = secondBatch * plotsReachableByStepCount[secondBatchStepsTaken + mapOffset]!!
        // Lets say the grid is completed on step 31. That means on step 32 it will go back to 39, then step 33 it will
        // go back to 42
        // lets say target is 50
        if (isEdge) {
            val b = numberOfCompletedGrids / 2
            val a = if (gridsCompleted % 2 == 0L) {
                numberOfCompletedGrids / 2
            } else {
                numberOfCompletedGrids / 2 + 1
            }
            val numberOfPlotsFromCompletedGrids  =
                a * plotsReachableBasedOnStepCount[evenTargetSteps]!! + b * plotsReachableBasedOnStepCount[oddTargetSteps]!!
            return grid to numberOfPlotsFromCompletedGrids + firstBatchValue + secondBatchValue

        } else {
            // If the repeat length is odd, we will alternate between odd and even
            // Evens
            val a = simpleArithmeticProgression(1, numberOfCompletedGrids, 2)
            // Odds -- true 39, false 42
            val b = simpleArithmeticProgression(2, numberOfCompletedGrids, 2)
            val numberOfPlotsReachableFromCompletedGrids =
                a * plotsReachableBasedOnStepCount[evenTargetSteps]!! + b * plotsReachableBasedOnStepCount[oddTargetSteps]!!
            return grid to numberOfPlotsReachableFromCompletedGrids + firstBatchValue + secondBatchValue
        }
    }


    private fun printGridAsDiamond(keys: MutableSet<Day17.Coordinate>) {
        val leftMostX = keys.minOf { it.x }
        val rightMostX = keys.maxOf { it.x }
        val topMostY = keys.maxOf { it.y }
        val bottomMostY = keys.minOf { it.y }
        for (y in topMostY downTo bottomMostY) {
            for (x in leftMostX..rightMostX) {
                val coord = Day17.Coordinate(x, y)
                if (coord in keys) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun isAGardenPlotExpanded(
        coordinate: Day17.Coordinate,
        gardenPlots: Set<Day17.Coordinate>,
        input: List<String>
    ): Boolean {
        val newX = coordinate.x.mod(input[0].length)
        val newY = coordinate.y.mod(input.size)
        return Day17.Coordinate(newX, newY) in gardenPlots
    }

    fun nextCoordinatesExpanded(
        coordinate: Day17.Coordinate,
        gardenPlots: Set<Day17.Coordinate>,
        input: List<String>
    ): List<Day17.Coordinate> {
        return listOf(
            Day17.Coordinate(coordinate.x - 1, coordinate.y),
            Day17.Coordinate(coordinate.x + 1, coordinate.y),
            Day17.Coordinate(coordinate.x, coordinate.y - 1),
            Day17.Coordinate(coordinate.x, coordinate.y + 1)
        ).filter { isAGardenPlotExpanded(it, gardenPlots, input) }
    }

    fun nextCoordinates(coordinate: Day17.Coordinate, gardenPlots: Set<Day17.Coordinate>): List<Day17.Coordinate> {
        return listOf(
            Day17.Coordinate(coordinate.x - 1, coordinate.y),
            Day17.Coordinate(coordinate.x + 1, coordinate.y),
            Day17.Coordinate(coordinate.x, coordinate.y - 1),
            Day17.Coordinate(coordinate.x, coordinate.y + 1)
        ).filter { it in gardenPlots }
    }

    fun arithmeticProgression(n: Long, a1: Long, d: Long = 2): Long {
        return (n.toDouble().div(2) * (a1 + an(n, a1, d))).toLong()
    }

    fun an(n: Long, a: Long, d: Long): Long {
        return a + (n - 1) * d
    }

    fun diamondSize(n: Long): Long {
        return arithmeticProgression(n - 1, 1) * 2 + an(n, 1, 2)
    }

    fun evenCoordinates(n: Long): Long {
        if (n % 2L == 0L) {
            return evenCoordinates(n - 1)
        }

        return n.toDouble().pow(2).toLong()
    }

    fun oddCoordinates(n: Long): Long {
        if (n % 2L == 1L) {
            return oddCoordinates(n - 1)
        }
        return n.toDouble().pow(2).toLong()
    }

    fun simpleArithmeticProgression(start: Long, n: Long, step: Long): Long {
        return (start..n).step(step).sum()
    }
}
