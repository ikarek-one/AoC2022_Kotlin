package tasks.day23

import base.Task
import java.lang.Exception
import tasks.day23.Direction.*
import utils.testAssertions


class UnstableDiffusion : Task {
    override val id: Int
        get() = 23

    companion object {
        const val partOneRoundNumber = 10

        // throws exception if more rounds is needed
        const val partTwoRoundLimit = 100000
    }

    override fun part1(lines: Sequence<String>): Long {
        val pointMap = parsePoints(lines)

        val directions = mutableListOf(
            arrayOf(NW, N, NE) to N,
            arrayOf(SE, S, SW) to S,
            arrayOf(SW, W, NW) to W,
            arrayOf(NE, E, SE) to E
        )

        fun rotateDirections() = directions.add(directions.removeFirst())

        repeat(partOneRoundNumber) {
            round(pointMap, directions)
            rotateDirections()
        }

        return rectangleArea(pointMap)
    }

    override fun part2(lines: Sequence<String>): Long {
        val pointMap = parsePoints(lines)

        val directions = mutableListOf(
            arrayOf(NW, N, NE) to N,
            arrayOf(SE, S, SW) to S,
            arrayOf(SW, W, NW) to W,
            arrayOf(NE, E, SE) to E
        )

        fun rotateDirections() = directions.add(directions.removeFirst())


        for (i in 1L..partTwoRoundLimit) {
            val elvesMoved = round(pointMap, directions)
            rotateDirections()

            if (elvesMoved == 0) {
                return i
            }
        }


        throw Exception("Limit of rounds = $partTwoRoundLimit has been reached and the moves hadn't stopped!")
    }

    private fun parsePoints(lines: Sequence<String>): MutableMap<Pair<Int, Int>, Point> {
        return lines
            .filter { it.isNotBlank() }
            .withIndex()
            .flatMap { (y, line) ->
                line.withIndex().map { (x, ch) ->
                    val isPointEmpty = when (ch) {
                        '#' -> false
                        '.' -> true
                        else -> throw Exception("Unexpected char = '$ch'!")
                    }
                    Point(x, y, isPointEmpty)
                }
            }.associate { point ->
                (point.x to point.y) to point
            }.toMutableMap()
    }

    private fun round(map: MutableMap<Pair<Int, Int>, Point>, directions: MutableList<Pair<Array<Direction>, Direction>>): Int {
        val propositions = map.toMap().values.asSequence()
            .filter { it.isNotEmpty() }
            // 'desired point' to 'point from which an elf wants to move'
            .map { it.neighbour(newDirection(it, map, directions)) to it }
            .mapNotNull { if (it.first == null) null else (it.first!! to it.second) }
            .toList()


        val grouped = propositions
            .groupBy(
                { it.first },
                { it.second }
            )
            .filter { it.value.size == 1 }

        grouped.forEach { (k, v) -> moveElf(v[0], map[k]!!) }

        return grouped.size
    }

    private fun moveElf(from: Point, to: Point) {
        to.isEmpty = false
        from.isEmpty = true
    }

    private fun newDirection(
        point: Point,
        map: MutableMap<Pair<Int, Int>, Point>,
        directions: MutableList<Pair<Array<Direction>, Direction>>
    ): Direction? {
        fun canGoTo(vararg dirs: Direction) =
            dirs.asSequence()
                .map { point.neighbour(it) }
                .all { map.computeIfAbsent(it!!, { pair -> Point(pair) }).isEmpty }

        return when {
            canGoTo(*Direction.values()) -> null
            canGoTo(*directions[0].first) -> directions[0].second
            canGoTo(*directions[1].first) -> directions[1].second
            canGoTo(*directions[2].first) -> directions[2].second
            canGoTo(*directions[3].first) -> directions[3].second
            else -> null
        }

    }

    private fun rectangleArea(pointMap: Map<Pair<Int, Int>, Point>): Long {
        val points = pointMap.values.filter { it.isNotEmpty() }
        val maxY = points.maxOf { it.y }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }
        val minX = points.minOf { it.x }

        return (minX..maxX)
            .flatMap { x ->
                (minY..maxY).map { y ->
                    pointMap[x to y]?.isNotEmpty() == true
                }
            }.count { ! it }.toLong()
    }

    data class Point(val x: Int, val y: Int, var isEmpty:Boolean = true) {
        constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

        fun isNotEmpty() = !this.isEmpty

        fun neighbour(direction: Direction?): Pair<Int, Int>? {
            return if (direction == null) null
            else (this.x + direction.x) to (this.y + direction.y)
        }

    }

}


fun main() {
    testAssertions(::UnstableDiffusion)
}