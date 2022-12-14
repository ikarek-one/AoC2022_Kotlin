package tasks.day14

import base.Task
import utils.testAssertions
import kotlin.math.max
import kotlin.math.min

fun main() {
    testAssertions(::RegolithReservoir)
}

class RegolithReservoir : Task {
    override val id: Int
        get() = 14

    private val occupiedPoints = mutableSetOf<Point>()
    private val START_POINT = Point(500, 0)


    override fun part1(lines: Sequence<String>): Long {
        lines
            .filter { it.isNotBlank() }
            .forEach { drawPath(it) }

        return generateSequence { snow() }
            .takeWhile { it }
            .count()
            .toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        this.occupiedPoints.clear()

        val points = lines.filter { it.isNotBlank() }.toList()
        points.forEach { drawPath(it) }

        val floorLevel = occupiedPoints.maxOf { it.y } + 2

        return generateSequence { snow({ it < floorLevel }, { false }) }
            .takeWhile { !this.occupiedPoints.contains(START_POINT) }
            .count()
            .toLong() + 1
    }

    private fun drawPath(line: String) {
        return line.replace("\\s+".toRegex(), "")
            .split("->")
            .map { it.split(",") }
            .map { tokens ->
                Point(tokens[0].toInt(), tokens[1].toInt())
            }.zipWithNext()
            .forEach { drawSingleLine(it.first, it.second) }
    }

    private fun drawSingleLine(from: Point, to: Point) {
        if (from.x == to.x) {
            for (y in min(from.y, to.y)..max(from.y, to.y)) {
                this.occupiedPoints.add(Point(from.x, y))
            }
        } else if (from.y == to.y) {
            for (x in min(from.x, to.x)..max(from.x, to.x)) {
                this.occupiedPoints.add(Point(x, from.y))
            }
        } else {
            throw Exception("Only diagonal or vertical lines were expected!")
        }
    }

    private fun snow(
        floorCondition: (Int) -> Boolean = { true },
        stopCondition: (Point) -> Boolean = { it.y > lowestPoint() }
    ): Boolean {
        var pos = START_POINT
        fun canGoTo(x: Int, y: Int) = !occupiedPoints.contains(Point(x, y)) && floorCondition(y)

        while (true) {
            pos = if (stopCondition(pos)) {
                return false
            } else if (canGoTo(pos.x, pos.y + 1)) {
                Point(pos.x, pos.y + 1)
            } else if (canGoTo(pos.x - 1, pos.y + 1)) {
                Point(pos.x - 1, pos.y + 1)
            } else if (canGoTo(pos.x + 1, pos.y + 1)) {
                Point(pos.x + 1, pos.y + 1)
            } else {
                return occupiedPoints.add(pos)
            }
        }
    }

    private fun lowestPoint(): Int {
        return occupiedPoints.maxOf { it.y }
    }

    private data class Point(val x: Int, val y: Int)
}
