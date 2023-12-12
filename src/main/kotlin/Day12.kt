import kotlin.String
import kotlin.collections.List

object Day12 {
    fun part1(input: List<String>): String {
        return input.sumOf {
            val (layout, contiguousSegments) = it.split(" ")
            val contiguousSegmentsQueue = ArrayDeque(contiguousSegments.split(",").map { it.toLong() })
            val firstContiguousSegmentSize = contiguousSegmentsQueue.removeFirst()
            possibleArrangements(layout, contiguousSegmentsQueue, false, firstContiguousSegmentSize, mutableMapOf())
        }.toString()
    }

    fun part2(input: List<String>): String {
        return input.sumOf {
            val (layout, contiguousSegments) = it.split(" ")
            val contiguousSegmentsQueue = ArrayDeque(contiguousSegments.split(",").map { it.toLong() })
            val repeatLength = 5
            val repeatedContiguousSegments = ArrayDeque(List(repeatLength) { contiguousSegmentsQueue }.flatten())
            val firstContigousSegmentSize = repeatedContiguousSegments.removeFirst()
            val repeatedLayout = List(repeatLength) { layout }.joinToString("?")
            possibleArrangements(
                repeatedLayout,
                repeatedContiguousSegments,
                false,
                firstContigousSegmentSize,
                mutableMapOf()
            )
        }.toString()
    }

    private fun possibleArrangements(
        layout: String,
        contiguousSegmentSizes: ArrayDeque<Long>,
        contiguousSegmentStarted: Boolean,
        currentContiguousSegmentSize: Long,
        memo: MutableMap<MemoKey, Long>
    ): Long {
        val memoKey = MemoKey(layout, contiguousSegmentSizes, contiguousSegmentStarted, currentContiguousSegmentSize)
        if (memo.containsKey(memoKey)) {
            return memo[memoKey]!!
        }
        if (contiguousSegmentSizes.isEmpty() && currentContiguousSegmentSize == 0L) {
            // There is not a possible arragement if there are no more contiguous segments to place and the layout still
            // has parts
            if (layout.any { it == '#' }) {
                return 0
            }
            return 1
        }

        if ((currentContiguousSegmentSize > 0 || contiguousSegmentSizes.any { it > 0 }) && isEmpty(layout)) {
            // There is not a possible arrangement if there are still contiguous segments to place and the layout is empty
            return 0
        }

        val newArrangements = ArrayDeque(contiguousSegmentSizes)
        if (currentContiguousSegmentSize == 0L) {
            if (layout[0] == '.') {
                val newArrangement = newArrangements.removeFirst()
                val ret = possibleArrangements(layout.substring(1), newArrangements, false, newArrangement, memo)
                memo[memoKey] = ret
                return ret
            } else if (layout[0] == '?') {
                val ret = possibleArrangements(
                    layout.replaceFirst('?', '.'),
                    newArrangements,
                    contiguousSegmentStarted,
                    currentContiguousSegmentSize,
                    memo
                )
                memo[memoKey] = ret
                return ret
            }
            // There is not a possible arrangement if the next layout needs a part but the contiguous segment size left is 0
            return 0
        }
        // At this point, I am guaranteed a non empty input, a non zero current arrangement count, and a non empty arrangements list
        if (layout[0] == '.') {
            // There is not a valid state if we need to place a part but the layout doesnt allow one
            if (contiguousSegmentStarted) {
                return 0
            }
            val ret =
                possibleArrangements(layout.substring(1), newArrangements, false, currentContiguousSegmentSize, memo)
            memo[memoKey] = ret
            return ret
        }

        if (layout[0] == '#') {
            val ret =
                possibleArrangements(layout.substring(1), newArrangements, true, currentContiguousSegmentSize - 1, memo)
            memo[memoKey] = ret
            return ret
        }
        if (layout[0] == '?') {
            val ret = if (contiguousSegmentStarted) {
                possibleArrangements(
                    layout.replaceFirst('?', '#'),
                    newArrangements,
                    true,
                    currentContiguousSegmentSize,
                    memo
                )
            } else {
                possibleArrangements(
                    layout.replaceFirst('?', '#'),
                    newArrangements,
                    contiguousSegmentStarted,
                    currentContiguousSegmentSize,
                    memo
                ) + possibleArrangements(
                    layout.replaceFirst('?', '.'),
                    newArrangements,
                    false,
                    currentContiguousSegmentSize,
                    memo
                )
            }
            memo[memoKey] = ret
            return ret
        }
        // Should never get here
        return 0
    }

    private fun isEmpty(input: String): Boolean {
        return input.isEmpty() || input.all { it == '.' }
    }

    data class MemoKey(
        val input: String,
        val arrangements: ArrayDeque<Long>,
        val segmentStarted: Boolean,
        val currentArrangementCount: Long
    )
}
