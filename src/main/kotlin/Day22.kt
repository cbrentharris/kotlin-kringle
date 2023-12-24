import java.lang.Exception
import kotlin.String
import kotlin.collections.List
import kotlin.math.max
import kotlin.math.min

public object Day22 {
    private val FLOOR = 1

    data class ThreeDCoordinate(val x: Int, val y: Int, val z: Int) : Comparable<ThreeDCoordinate> {
        override fun compareTo(other: ThreeDCoordinate): Int {
            return compareValuesBy(this, other, { it.z }, { it.y }, { it.x })
        }
    }

    data class Brick(val start: ThreeDCoordinate, val end: ThreeDCoordinate) {
        fun supports(other: Brick): Boolean {
            if (this == other) return false
            val zIsDirectlyBelow = max(this.start.z, this.end.z) == min(other.start.z, other.end.z) - 1
            return zIsDirectlyBelow &&
                    this.overlaps(other)
        }

        private fun zLength(): Int {
            return max(this.start.z, this.end.z) - min(this.start.z, this.end.z)
        }

        fun fallV2(bricks: List<Brick>, memo: MutableMap<Brick, Int>): Brick {
            val stoppingMax = landingZCoordinate(bricks, memo)
            return if (this.start.z == this.end.z) {
                this.copy(start = start.copy(z = stoppingMax), end = end.copy(z = stoppingMax))
            } else if (this.start.z < this.end.z) {
                this.copy(
                    start = start.copy(z = stoppingMax),
                    end = end.copy(z = stoppingMax + zLength())
                )
            } else {
                this.copy(
                    start = start.copy(z = stoppingMax + zLength()),
                    end = end.copy(z = stoppingMax)
                )
            }
        }

        fun landingZCoordinate(bricks: List<Brick>, memo: MutableMap<Brick, Int>): Int {
            if (this in memo) {
                return memo[this]!!
            }
            val bricksBelow = bricks.filter { it.zBelow(this) }
                .filter { overlaps(it) }
            return if (bricksBelow.isEmpty()) {
                memo[this] = FLOOR
                FLOOR
            } else {
                val landingZCoordinates = bricksBelow.map { it to it.landingZCoordinate(bricks, memo) }
                val landingZCoordinate = landingZCoordinates
                    .maxOfOrNull { it.second + it.first.zLength() + 1 }
                    ?: throw Exception("Cannot find stopping max for node: $this")
                memo[this] = landingZCoordinate
                landingZCoordinate
            }
        }

        private fun zBelow(brick: Brick): Boolean {
            return max(this.start.z, this.end.z) < min(brick.start.z, brick.end.z)
        }

        private fun overlaps(other: Brick): Boolean {
            return this.xOverlaps(other) && this.yOverlaps(other)
        }

        fun xOverlaps(other: Brick): Boolean {
            val thisMin = min(this.start.x, this.end.x)
            val thisMax = max(this.start.x, this.end.x)
            val otherMin = min(other.start.x, other.end.x)
            val otherMax = max(other.start.x, other.end.x)

            return overlaps(thisMin..thisMax, otherMin..otherMax)

        }

        fun overlaps(a: ClosedRange<Int>, b: ClosedRange<Int>): Boolean {
            return a.contains(b.start) || a.contains(b.endInclusive) || b.contains(a.start) || b.contains(a.endInclusive)
        }

        fun yOverlaps(other: Brick): Boolean {
            val thisMin = min(this.start.y, this.end.y)
            val thisMax = max(this.start.y, this.end.y)
            val otherMin = min(other.start.y, other.end.y)
            val otherMax = max(other.start.y, other.end.y)
            return overlaps(thisMin..thisMax, otherMin..otherMax)
        }

        companion object {
            fun parse(input: String): Brick {
                val (start, end) = input.split("~").map { parseCoordinate(it) }.sorted()
                return Brick(start, end)
            }

            private fun parseCoordinate(input: String): ThreeDCoordinate {
                val (x, y, z) = input.split(",").map { it.toInt() }
                return ThreeDCoordinate(x, y, z)
            }
        }
    }

    public fun part1(input: List<String>): String {
        val bricks = input.map { Brick.parse(it) }
        val memo = mutableMapOf<Brick, Int>()
        val fallen = bricks.map { it.fallV2(bricks, memo) }
        val supportedBy = fallen.map { brick ->
            brick to fallen.filter { it.supports(brick) }
        }.toMap()
        val supporting = fallen.map { brick ->
            brick to fallen.filter { brick.supports(it) }
        }.toMap()
        val bricksThatAreNotTheOnlyBrickSupportingAnotherBrick = supporting.count { (k, v) ->
            v.all { supportedBy[it]!!.size > 1 }
        }
        return bricksThatAreNotTheOnlyBrickSupportingAnotherBrick.toString()
    }

    public fun part2(input: List<String>): String {
        val bricks = input.map { Brick.parse(it) }
        val memo = mutableMapOf<Brick, Int>()
        val fallen = bricks.map { it.fallV2(bricks, memo) }
        val supportedBy = fallen.map { brick ->
            brick to fallen.filter { it.supports(brick) }
        }.toMap()
        val supporting = fallen.map { brick ->
            brick to fallen.filter { brick.supports(it) }
        }.toMap()
        return fallen.sumOf { brick ->
            val ret = bricksThatWouldFallIfDeleted(brick, mutableSetOf(), supportedBy, supporting, mutableMapOf())
            ret.size
        }.toString()
    }

    private fun bricksThatWouldFallIfDeleted(
        brick: Brick,
        runningBricksFallen: MutableSet<Brick>,
        supportedBy: Map<Brick, List<Brick>>,
        supporting: Map<Brick, List<Brick>>,
        memo: MutableMap<Brick, Set<Brick>>
    ): Set<Brick> {
        if (brick in memo) {
            return memo[brick]!!
        }
        val bricksThatWillFall =
            supporting[brick]!!.filter { brickSupported ->
                supportedBy[brickSupported]!!.filter { it !in runningBricksFallen }.filter { it != brick }.isEmpty()
            }.toSet()
        runningBricksFallen.addAll(bricksThatWillFall)
        val cascadedFallingBricks = bricksThatWillFall + bricksThatWillFall.flatMap { cascadeBrick ->
            bricksThatWouldFallIfDeleted(
                cascadeBrick,
                runningBricksFallen,
                supportedBy,
                supporting,
                memo
            )
        }
        memo[brick] = cascadedFallingBricks
        return cascadedFallingBricks
    }
}
