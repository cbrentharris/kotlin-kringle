import kotlin.String
import kotlin.collections.List

object Day10 {
    data class Coordinate(val x: Int, val y: Int)

    data class Node(val coordinate: Coordinate, val pipe: String, val connections: List<Coordinate>)

    fun part1(input: List<String>): String {
        val graph = parse(input)
        val nodesInLoop = findNodesInLoop(graph)
        val graphWithJunkRemoved = graph.filterKeys { it in nodesInLoop }
        return djikstras(graphWithJunkRemoved).values.max().toString()
    }

    private fun djikstras(graph: Map<Coordinate, Node>): Map<Coordinate, Int> {
        val startNode = graph.values.find { it.pipe == "S" }!!
        val dists = mutableMapOf<Coordinate, Int>()
        val unvisited = mutableSetOf<Coordinate>()
        for (node in graph.values) {
            unvisited.add(node.coordinate)
        }
        dists[startNode.coordinate] = 0
        while (unvisited.isNotEmpty()) {
            val current = unvisited.minBy { dists[it] ?: Int.MAX_VALUE }
            unvisited.remove(current)
            for (connection in graph[current]?.connections ?: emptyList()) {
                val alt = dists.getOrPut(current) { 0 } + 1
                if (alt < (dists[connection] ?: Int.MAX_VALUE)) {
                    dists[connection] = alt
                }
            }
        }
        return dists
    }

    fun parse(input: List<String>): Map<Coordinate, Node> {
        val split = input.map { it.split("").filter { it.isNotBlank() } }
        val northDestinations = setOf("|", "7", "F", "S")
        val southDestinations = setOf("|", "J", "L", "S")
        val eastDestinations = setOf("-", "7", "J", "S")
        val westDestinations = setOf("-", "F", "L", "S")
        val nodes = split.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, char ->
                val coordinate = Coordinate(x, y)
                when (char) {
                    "|" -> {
                        val connections = listOf(
                            split.getOrNull(y - 1)?.getOrNull(x)
                                ?.let { if (northDestinations.contains(it)) Coordinate(x, y - 1) else null },
                            split.getOrNull(y + 1)?.getOrNull(x)
                                ?.let { if (southDestinations.contains(it)) Coordinate(x, y + 1) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "-" -> {
                        val connections = listOf(
                            split.getOrNull(y)?.getOrNull(x - 1)
                                ?.let { if (westDestinations.contains(it)) Coordinate(x - 1, y) else null },
                            split.getOrNull(y)?.getOrNull(x + 1)
                                ?.let { if (eastDestinations.contains(it)) Coordinate(x + 1, y) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "L" -> {
                        val connections = listOf(
                            split.getOrNull(y - 1)?.getOrNull(x)
                                ?.let { if (northDestinations.contains(it)) Coordinate(x, y - 1) else null },
                            split.getOrNull(y)?.getOrNull(x + 1)
                                ?.let { if (eastDestinations.contains(it)) Coordinate(x + 1, y) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "J" -> {
                        val connections = listOf(
                            split.getOrNull(y - 1)?.getOrNull(x)
                                ?.let { if (northDestinations.contains(it)) Coordinate(x, y - 1) else null },
                            split.getOrNull(y)?.getOrNull(x - 1)
                                ?.let { if (westDestinations.contains(it)) Coordinate(x - 1, y) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "7" -> {
                        val connections = listOf(
                            split.getOrNull(y + 1)?.getOrNull(x)
                                ?.let { if (southDestinations.contains(it)) Coordinate(x, y + 1) else null },
                            split.getOrNull(y)?.getOrNull(x - 1)
                                ?.let { if (westDestinations.contains(it)) Coordinate(x - 1, y) else null },
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "F" -> {
                        val connections = listOf(
                            split.getOrNull(y + 1)?.getOrNull(x)
                                ?.let { if (southDestinations.contains(it)) Coordinate(x, y + 1) else null },
                            split.getOrNull(y)?.getOrNull(x + 1)
                                ?.let { if (eastDestinations.contains(it)) Coordinate(x + 1, y) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    "S" -> {
                        val connections = listOf(
                            split.getOrNull(y - 1)?.getOrNull(x)
                                ?.let { if (northDestinations.contains(it)) Coordinate(x, y - 1) else null },
                            split.getOrNull(y + 1)?.getOrNull(x)
                                ?.let { if (southDestinations.contains(it)) Coordinate(x, y + 1) else null },
                            split.getOrNull(y)?.getOrNull(x - 1)
                                ?.let { if (westDestinations.contains(it)) Coordinate(x - 1, y) else null },
                            split.getOrNull(y)?.getOrNull(x + 1)
                                ?.let { if (eastDestinations.contains(it)) Coordinate(x + 1, y) else null }
                        ).filterNotNull()
                        Node(coordinate, char, connections)
                    }

                    else -> null
                }
            }
        }.flatten()
        return nodes.associateBy { it.coordinate }
    }

    fun part2(input: List<String>): String {
        val graph = parse(input)
        val nodesInLoop = findNodesInLoop(graph)
        val inputWithJunkPipesRemoved = input.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, char ->
                val coordinate = Coordinate(x, y)
                if (char == '.' || coordinate !in nodesInLoop) {
                    '.'.toString()
                } else {
                    char.toString()
                }
            }.joinToString("")
        }
        val graphWithJunkRemoved = graph.filterKeys { it in nodesInLoop }
        val graphWithStartReplaced = graphWithJunkRemoved.map { (coordinate, node) ->
            if (node.pipe == "S") {
                coordinate to Node(coordinate, translate("S", node.connections), node.connections)
            } else {
                coordinate to node
            }
        }.toMap()
        val dots = inputWithJunkPipesRemoved.mapIndexed { y, row ->
            row.mapIndexedNotNull { x, char ->
                val coordinate = Coordinate(x, y)
                if (char == '.') {
                    coordinate
                } else {
                    null
                }
            }
        }.flatten().toSet()
        return dots.count { isInPolygon(graphWithStartReplaced, it) }.toString()
    }

    private fun translate(s: String, connections: List<Coordinate>): String {
        val sortedConnections = connections.sortedWith(compareBy({ it.x }, { it.y }))
        val northSouth = (sortedConnections.first().x - sortedConnections.last().x) == 0
        val eastWest = (sortedConnections.first().y - sortedConnections.last().y) == 0
        val eastNorth =
            sortedConnections.first().x > sortedConnections.last().x && sortedConnections.first().y < sortedConnections.last().y
        val eastSouth =
            sortedConnections.first().x > sortedConnections.last().x && sortedConnections.first().y > sortedConnections.last().y
        val westSouth =
            sortedConnections.first().x < sortedConnections.last().x && sortedConnections.first().y < sortedConnections.last().y
        val westNorth =
            sortedConnections.first().x < sortedConnections.last().x && sortedConnections.first().y > sortedConnections.last().y
        return when {
            eastWest -> "-"
            northSouth -> "|"
            eastNorth -> "L"
            eastSouth -> "F"
            westNorth -> "J"
            westSouth -> "7"
            else -> s
        }
    }

    private fun findNodesInLoop(graph: Map<Coordinate, Node>): Set<Coordinate> {
        val startNode = graph.values.find { it.pipe == "S" }!!
        val processed = mutableSetOf<Coordinate>()
        val unprocessed = ArrayDeque<Coordinate>()
        unprocessed.add(startNode.coordinate)
        while (unprocessed.isNotEmpty()) {
            val current = unprocessed.removeFirst()
            if (current in processed) {
                continue
            }
            unprocessed.addAll(graph[current]?.connections ?: emptyList())
            processed.add(current)
        }
        return processed
    }

    /**
     * Modified ray cast algorithm to identify if a coordinate is within the loop.
     *
     * Instead of casting a simple ray any direction and seeing if it crosses an odd number of segments,
     * we cast rays in each direction and see if they cross an odd number of segments. Additionally
     * we collapse segments such as
     *     F--
     *     |
     *   --J
     *
     * into a single segment FJ
     */
    private fun isInPolygon(graph: Map<Coordinate, Node>, it: Coordinate): Boolean {
        return isInPolygonAbove(graph, it) && isInPolygonBelow(graph, it) && isInPolygonLeft(
            graph,
            it
        ) && isInPolygonRight(graph, it)
    }

    /**
     * Modified ray cast algorithm to identify num segments right
     *
     * ------> ||| (true, 3)
     * ------> || (false, 2)
     */
    fun isInPolygonRight(graph: Map<Coordinate, Node>, coordinate: Coordinate): Boolean {
        val intersectionsRight = graph.keys.filter { it.x > coordinate.x }.filter { it.y == coordinate.y }
        val directSegments = intersectionsRight
            .filter { graph[it]?.pipe == "|" }
        val indirectSegmentRightPairs = setOf("FJ", "L7")
        val indirectSegments = intersectionsRight
            .filter { graph[it]?.pipe != "-" }
            .zipWithNext().filter { (a, b) ->
                (graph[a]?.pipe + graph[b]?.pipe) in indirectSegmentRightPairs
            }
        return (indirectSegments.size + directSegments.size) % 2 == 1
    }

    /**
     * Modified ray cast algorithm to identify num segments left
     *
     * ||| <------ (true, 3)
     * || <------ (false, 2)
     */
    fun isInPolygonLeft(graph: Map<Coordinate, Node>, coordinate: Coordinate): Boolean {
        val intersectionsLeft =
            graph.keys.filter { it.x < coordinate.x }.filter { it.y == coordinate.y }.sortedByDescending { it.x }
        val directSegments = intersectionsLeft
            .filter { graph[it]?.pipe == "|" }
        val indirectSegmentLeftPairs = setOf("JF", "7L")
        val indirectSegments = intersectionsLeft
            .filter { graph[it]?.pipe != "-" }
            .zipWithNext().filter { (a, b) ->
                (graph[a]?.pipe + graph[b]?.pipe) in indirectSegmentLeftPairs
            }
        return (indirectSegments.size + directSegments.size) % 2 == 1
    }

    /**
     * Modified ray cast algorithm to identify num segments below
     *
     *  |
     *  |
     *  v
     *  -
     *  -
     *  - (true, 3)
     *
     *  |
     *  |
     *  v
     *  -
     *  - (false, 2)
     */
    fun isInPolygonBelow(graph: Map<Coordinate, Node>, coordinate: Coordinate): Boolean {
        val intersectionsBelow =
            graph.keys.filter { it.y > coordinate.y }.filter { it.x == coordinate.x }.sortedBy { it.y }
        val directSegments = intersectionsBelow
            .filter { graph[it]?.pipe == "-" }
        val indirectSegmentSouthPairs = setOf("FJ", "7L")
        val indirectSegments = intersectionsBelow
            .filter { graph[it]?.pipe != "|" }
            .zipWithNext().filter { (a, b) ->
                (graph[a]?.pipe + graph[b]?.pipe) in indirectSegmentSouthPairs
            }
        return (indirectSegments.size + directSegments.size) % 2 == 1
    }

    /**
     * Modified ray cast algorithm to identify num segments above
     *
     *  - (true, 3)
     *  -
     *  -
     *  ^
     *  |
     *  |
     *
     *  - (false, 2)
     *  -
     *  ^
     *  |
     *  |
     */
    fun isInPolygonAbove(graph: Map<Coordinate, Node>, coordinate: Coordinate): Boolean {
        graph.keys.filter { it.y < coordinate.y }.filter { it.x < coordinate.x }
        val intersectionsAbove =
            graph.keys.filter { it.y < coordinate.y }.filter { it.x == coordinate.x }.sortedByDescending { it.y }
        val directSegments = intersectionsAbove
            .filter { graph[it]?.pipe == "-" }
        val indirectSegmentNorthPairs = setOf("JF", "L7")
        val indirectSegments = intersectionsAbove
            .filter { graph[it]?.pipe != "|" }
            .zipWithNext().filter { (a, b) ->
                (graph[a]?.pipe + graph[b]?.pipe) in indirectSegmentNorthPairs
            }
        return (indirectSegments.size + directSegments.size) % 2 == 1
    }
}
