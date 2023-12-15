import kotlin.String
import kotlin.collections.List

object Day15 {

    fun part1(input: List<String>): String {
        val steps = input[0].split(",").map { it.trim() }
        return steps.sumOf { hash(it) }.toString()
    }

    private fun hash(it: String): Int {
        return it.toCharArray().fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
    }

    fun part2(input: List<String>): String {
        val steps = input[0].split(",").map { it.trim() }
        val boxes = mutableMapOf<Int, LinkedHashMap<String, Int>>()
        steps.forEach {
            if (it.contains("-")) {
                val label = it.substring(0, it.length - 1)
                val box = hash(label)
                boxes.getOrPut(box) { linkedMapOf() }.remove(label)
            } else {
                val (label, focusLength) = it.split("=")
                val box = hash(label)
                val orderedMap = boxes.getOrPut(box) { linkedMapOf() }
                orderedMap[label] = focusLength.toInt()
            }
        }
        return boxes.flatMap { (boxNumber, lenses) ->
            val boxNumberOffset = boxNumber + 1
            lenses.values.withIndex().map { (idx, value) ->
                val ret = (idx + 1) * value * boxNumberOffset
                ret
            }
        }.sum().toString()
    }
}
