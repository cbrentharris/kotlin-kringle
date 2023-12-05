import java.lang.Long.min
import kotlin.String
import kotlin.collections.List

object Day5 {
    data class Range(val start: Long, val end: Long)
    data class RangeMapping(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {
        fun next(id: Long): Long {
            return if (sourceRangeStart <= id && id <= sourceRangeStart + rangeLength) {
                val offset = id - sourceRangeStart
                destinationRangeStart + offset
            } else {
                id
            }
        }

        fun next(range: Range): Range {
            val offset = range.start - sourceRangeStart
            return Range(destinationRangeStart + offset, destinationRangeStart + offset + range.end - range.start)
        }

        fun overlaps(id: Long): Boolean {
            return sourceRangeStart <= id && id < sourceRangeStart + rangeLength
        }

        fun overlaps(range: Range): Boolean {
            return sourceRangeStart <= range.start && range.end <= sourceRangeStart + rangeLength - 1
        }
    }

    data class Mapping(
        val category: String,
        val rangeMappings: List<RangeMapping>
    ) {

        fun fork(range: Range): List<Range> {
            if (rangeMappings.any { it.overlaps(range) }) {
                return listOf(range)
            }
            var start = range.start
            val end = range.end

            val ranges = mutableListOf<Range>()
            while (start <= end) {
                val closestWithoutGoingOver =
                    rangeMappings.filter { it.sourceRangeStart <= start && start < it.sourceRangeStart + it.rangeLength }
                        .minByOrNull { it.sourceRangeStart - range.start }
                if (closestWithoutGoingOver != null) {
                    val newEnd =
                        min(end, closestWithoutGoingOver.sourceRangeStart + closestWithoutGoingOver.rangeLength - 1)
                    ranges.add(Range(start, newEnd))
                    start = newEnd + 1
                } else {
                    val closestWithGoingOver =
                        rangeMappings.filter { it.sourceRangeStart + it.rangeLength > start }
                            .minByOrNull { it.sourceRangeStart - start }
                    if (closestWithGoingOver == null) {
                        ranges.add(Range(start, end))
                        start = end + 1
                    } else {
                        val newEnd = min(end, closestWithGoingOver.sourceRangeStart - 1)
                        ranges.add(Range(start, newEnd))
                        start = newEnd + 1
                    }
                }
            }
            return ranges
        }

        companion object {
            fun parseMappings(input: List<String>): List<Mapping> {
                var remainingInput = input
                val mappings = mutableListOf<Mapping>()
                while (remainingInput.isNotEmpty()) {
                    val category = remainingInput.first().split(" ").first()
                    val ranges = remainingInput.drop(1).takeWhile { it.isNotEmpty() }.map {
                        val (destinationRangeStart, sourceRangeStart, rangeLength) = it.split(" ")
                        RangeMapping(
                            destinationRangeStart.toLong(),
                            sourceRangeStart.toLong(),
                            rangeLength.toLong()
                        )
                    }
                    mappings.add(Mapping(category, ranges))
                    remainingInput = remainingInput.drop(ranges.size + 2)
                }
                return mappings
            }
        }
    }

    fun part1(input: List<String>): String {
        val seeds = input.first().replace("seeds: ", "")
            .split(" ")
            .map { it.toLong() }
        val mappings = Mapping.parseMappings(input.drop(2))
        val finalMappings = mappings.fold(seeds) { acc, mapping ->
            acc.map { id ->
                val range = mapping.rangeMappings.find { it.overlaps(id) }
                range?.next(id) ?: id
            }
        }
        return finalMappings.min().toString()
    }

    fun part2(input: List<String>): String {
        val ranges = input.first().replace("seeds: ", "")
            .split(" ")
            .map { it.toLong() }
            .chunked(2)
            .map { Range(it.first(), it.first() + it.last()) }
        val mappings = Mapping.parseMappings(input.drop(2))
        val finalRanges = mappings.fold(ranges) { transformedRanges, mapping ->
            transformedRanges.flatMap { range ->
                val nextRanges = mapping.fork(range).map { forkedRange ->
                    val overlaps = mapping.rangeMappings.filter { it.overlaps(forkedRange) }
                        .minByOrNull { it.sourceRangeStart - forkedRange.start }
                    overlaps?.next(forkedRange) ?: forkedRange
                }
                nextRanges
            }
        }
        return finalRanges.minBy { it.start }.start.toString()
    }
}
