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
                boxes.getOrPut(box) { linkedMapOf() }[label] = focusLength.toInt()
            }
        }
        return boxes.toList().sumOf { (boxNumber, lenses) ->
            lenses.values.withIndex().sumOf { (idx, value) ->
                (idx + 1) * value * (boxNumber + 1)
            }
        }.toString()
    }
}
