import kotlin.String
import kotlin.collections.List

object Day8 {
    data class Node(var left: Node?, var right: Node?, val key: String)
    data class DesertMap(val directions: CharArray, val root: Node?) {
        companion object {
            fun parse(input: List<String>): DesertMap {
                val directions = input.first().toCharArray()
                val nodes = parseNodes(input.drop(2))
                val root = nodes.find { it.key == "AAA" }
                return DesertMap(directions, root)
            }

            fun parsePart2(input: List<String>): List<DesertMap> {
                val directions = input.first().toCharArray()
                val nodes = parseNodes(input.drop(2))
                val roots = nodes.filter { it.key.endsWith("A") }
                return roots.map { DesertMap(directions, it) }
            }

            private fun parseNodes(input: List<String>): List<Node> {
                val index = mutableMapOf<String, Node>()
                val nodePattern = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex()
                for (line in input) {
                    val (key, left, right) = nodePattern.matchEntire(line)!!.destructured
                    val leftNode = index.getOrPut(left) { Node(null, null, left) }
                    val rightNode = index.getOrPut(right) { Node(null, null, right) }
                    val keyNode = index.getOrPut(key) { Node(null, null, key) }
                    keyNode.left = leftNode
                    keyNode.right = rightNode
                }
                return index.values.toList()
            }

        }
    }

    fun part1(input: List<String>): String {
        val desertMap = DesertMap.parse(input)
        return followUntilExit(desertMap.directions, desertMap.root, 0).toString()
    }

    private tailrec fun followUntilExit(
        directions: CharArray,
        currentNode: Node?,
        currentSteps: Long,
        exitCondition: (Node) -> Boolean = { it.key == "ZZZ" }
    ): Long {
        if (currentNode == null || exitCondition(currentNode)) {
            return currentSteps
        }
        val nextNode = if (directions.first() == 'L') {
            currentNode.left
        } else {
            currentNode.right
        }
        return followUntilExit(directions.rotate(1), nextNode, currentSteps + 1, exitCondition)
    }

    fun part2(input: List<String>): String {
        val desertMaps = DesertMap.parsePart2(input)
        return followUntilAllExit(desertMaps).toString()
    }

    private fun followUntilAllExit(desertMaps: List<DesertMap>): Long {
        val requiredStepsUntilExit =
            desertMaps.map { followUntilExit(it.directions, it.root, 0) { it.key.endsWith("Z") } }
        return leastCommonMultiple(requiredStepsUntilExit)
    }

    private fun leastCommonMultiple(requiredStepsUntilExit: List<Long>): Long {
        return requiredStepsUntilExit.reduce { a, b -> lcm(a, b) }
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * b / gcd(a, b)
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun CharArray.rotate(n: Int) =
        let { sliceArray(n..<size) + sliceArray(0..<n) }

}
