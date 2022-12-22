package tasks.day22

import base.Task
import utils.testAssertions



fun main() {
    testAssertions(::MonkeyMap)
}


class MonkeyMap: Task {
    override val id: Int
        get() = 22

    override fun part1(lines: Sequence<String>): Long {
        val allLines = lines.toList()
        val points = parsePoints(allLines)
        val passwordLine = allLines.last { it.isNotBlank() }

        val actions = parsePasswordLine(passwordLine)
        val initialPoint = points.minBy { it.x + it.y }
        val player = Player(initialPoint.x, initialPoint.y)

        movesPartOne(actions, player, points)

        return (1000 * player.y + 4 * player.x + player.heading.ordinal).toLong()
    }

    private fun movesPartOne(
        actions: List<Action>,
        player: Player,
        points: List<Point>
    ) {
        for (action in actions) {
            when (action) {
                is Rotate -> player.rotateHeading(action.direction)
                is MoveForward -> {
                    for (i in 0 until action.steps) {
                        val newPos = when (player.heading) {
                            Player.Heading.RIGHT ->
                                points.firstOrNull { it.x == player.x + 1 && it.y == player.y }
                                    ?: points.filter { it.y == player.y }.minBy { it.x }

                            Player.Heading.LEFT ->
                                points.firstOrNull { it.x == player.x - 1 && it.y == player.y }
                                    ?: points.filter { it.y == player.y }.maxBy { it.x }

                            Player.Heading.UP ->
                                points.firstOrNull { it.x == player.x && it.y == player.y - 1 }
                                    ?: points.filter { it.x == player.x }.maxBy { it.y }

                            Player.Heading.DOWN ->
                                points.firstOrNull { it.x == player.x && it.y == player.y + 1 }
                                    ?: points.filter { it.x == player.x }.minBy { it.y }
                        }

                        if (newPos.type == Point.Type.WALL) {
                            break
                        } else {
                            player.x = newPos.x
                            player.y = newPos.y
                        }
                    }
                }
            }
        }
    }

    private fun parsePoints(lines: List<String>) = lines
        .asSequence()
        .takeWhile { it.isNotBlank() }
        .withIndex()
        .flatMap { (y, line) ->
            line.withIndex().mapNotNull { (x, ch) ->
                when (ch) {
                    '.' -> Point(x + 1, y + 1, Point.Type.TILE)
                    '#' -> Point(x + 1, y + 1, Point.Type.WALL)
                    ' ' -> null
                    else -> throw Exception("Unexpected char = '$ch'!")
                }
            }
        }.toList()


    private fun parsePasswordLine(line: String): List<Action> {
        var buffer = ""
        var i = 0
        val actions = mutableListOf<Action>()

        while (i < line.length) {
            if (line[i] in listOf('L', 'R')) {
                actions.add(Rotate(if (line[i]=='L') Rotate.Direction.LEFT else Rotate.Direction.RIGHT))
                i += 1
            } else {
                while (i < line.length && line[i].isDigit()) {
                    buffer += line[i]
                    i += 1
                }
                actions.add(MoveForward(buffer.toInt()))
                buffer = ""
            }
        }

        return actions.toList()
    }

    data class Point(val x:Int, val y:Int, val type:Type) {
        enum class Type {
            TILE,
            WALL
        }

    }

    private data class Player(var x:Int, var y:Int, var heading:Heading = Heading.RIGHT ) {
        fun rotateHeading(dir: Rotate.Direction) {
            this.heading = this.heading.rotate(dir)
        }
        enum class Heading {
            RIGHT,
            DOWN,
            LEFT,
            UP;
            fun rotate(dir: Rotate.Direction): Heading {
                val len = Heading.values().size
                val nextOrdinal = when (dir) {
                    Rotate.Direction.LEFT -> (this.ordinal + len - 1) % len
                    Rotate.Direction.RIGHT -> (this.ordinal + 1) % len
                }
                return Heading.values()[nextOrdinal]
            }
        }

    }


    private sealed class Action
    private data class MoveForward(val steps: Int):Action()
    private data class Rotate(val direction: Direction):Action() {
        enum class Direction {
            LEFT,
            RIGHT;
        }
    }


    private fun debugPrint(points: Collection<Point>) {
        println("POINTS:   ")
        val xLen = points.maxOf { it.x }
        val yLen = points.maxOf { it.y }

        val space = ' '

        val grid = Array(yLen){
            Array(xLen) {_ -> space}
        }

        for (p in points) {
            grid[p.y - 1][p.x - 1] = when (p.type) {
                Point.Type.WALL -> '#'
                Point.Type.TILE -> '.'
            }
        }

        for (y in grid.indices) {
            println(grid[y].joinToString(separator = ""))
        }
        println("-----------------------------------------------")
    }
}



//override fun part2(lines: Sequence<String>): Long? {
//    val allLines = lines.toList()
//    val points = parsePoints(allLines)
//    val passwordLine = allLines.last { it.isNotBlank() }
//    val actions = parsePass2(passwordLine)
//    val initialPoint = points.minBy { it.x + it.y }
//    val player = Player(initialPoint.x, initialPoint.y)
//
//
//    val cubeWallLen = points.filter { it.x == 1 }.size
//
//
//
//
//
//    return super.part2(lines)
//}
//
//private fun whichFace(p: Point, cubeSide: Int):Int {
//    return when {
//        p.y in 1..cubeSide && p.x in 2 * cubeSide + 1 .. 3 * cubeSide -> 1
//        p.y in cubeSide + 1..2*cubeSide && p.x in 1..cubeSide -> 2
//        p.y in cubeSide + 1..2*cubeSide && p.x in cubeSide + 1..2*cubeSide -> 3
//        p.y in cubeSide + 1..2*cubeSide && p.x in 2 * cubeSide + 1..3*cubeSide -> 4
//        p.y in 2 * cubeSide + 1..3*cubeSide && p.x in 2 * cubeSide + 1..3*cubeSide -> 5
//        p.y in 2 * cubeSide + 1..3*cubeSide && p.x in 3 * cubeSide + 1..4*cubeSide -> 6
//        else -> throw Exception("Can't find a cube face for point = '$p'")
//    }
//}
