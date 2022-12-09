package tasks.day09

import base.Task
import utils.testAssertions
import kotlin.math.absoluteValue
import kotlin.math.sign

class RopeBridge : Task {
    override val id: Int
        get() = 9


    override fun part1(lines: Sequence<String>): Long {
        val moves = lines.filter { it.isNotBlank() }.toList()

        val head = RopePart()
        val tail = RopePart()

        for (move in moves) {
            val tokens = move.split("\\s+".toRegex())
            val direction = when (val cmd = tokens[0]) {
                "R" -> 1 to 0
                "L" -> -1 to 0
                "U" -> 0 to 1
                "D" -> 0 to -1
                else -> throw Exception("Only letters R, L, U, D expected but found text = '$cmd'")
            }

            repeat(tokens[1].toInt()) {
                head.moveBy(direction)
                tail.moveTowards(head)
            }
        }
        return tail.visitedPoints.distinct().size.toLong()
    }

}

data class RopePart(var x: Int = INITIAL_POINT.first, var y: Int = INITIAL_POINT.second) {
    val visitedPoints = mutableListOf(INITIAL_POINT)

    fun moveTowards(head: RopePart) {
        val deltaX = head.x - this.x
        val deltaY = head.y - this.y

        if (deltaX.absoluteValue > 1 || deltaY.absoluteValue > 1) {
            this.x += deltaX.sign
            this.y += deltaY.sign
        }
        visitedPoints.add(this.x to this.y)
    }

    fun moveBy(move: Pair<Int, Int>) {
        this.x += move.first
        this.y += move.second
    }

    companion object {
        val INITIAL_POINT: Pair<Int, Int> = 0 to 0
    }
}


fun main() {
    testAssertions(RopeBridge())
}
