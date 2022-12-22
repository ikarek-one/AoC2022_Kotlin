package tasks.day22

import base.Task
import utils.fromConsolePartOne
import utils.testAssertions
import java.util.Scanner

fun main() {
//    testAssertions(::MonkeyMap)
    fromConsolePartOne(MonkeyMap())
}

class MonkeyMap: Task {

    override val id: Int
        get() = 22

    override fun part1(lines: Sequence<String>): Long? {
        val allLines = lines.toList()

        val points = parsePoints(allLines)

        debugPrint(points)

        val passwordLine = allLines.last { it.isNotBlank() }

//        val actions = parsePasswordLine(passwordLine)
        val actions = parsePass2(passwordLine)
        println("actions = ${actions}")

        val initialPoint = points.minBy { it.x + it.y }
        println("initialPoint = $initialPoint")
        val player = Player(initialPoint.x, initialPoint.y)

        for (action in actions) {
            when (action) {
                is Rotate -> player.rotateHeading(action.direction)
                is MoveForward -> {
                    for (i in 0 until action.steps) {
                        val newPos = when (player.heading) {
                            Player.Heading.RIGHT ->
                                points.firstOrNull { it.x == player.x + 1 && it.y == player.y}
                                    ?: points.filter { it.y == player.y }.minBy { it.x }
                            Player.Heading.LEFT ->
                                points.firstOrNull { it.x == player.x - 1 && it.y == player.y}
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


        println(player)

        return (1000 * player.y + 4 * player.x + player.heading.ordinal).toLong()
    }


    private fun debugPrint(points: Collection<Point>) {
        println("POINTS:   ")
        val xLen = points.maxOf { it.x }
        val yLen = points.maxOf { it.y }

        val space = ' '

        val grid = Array(yLen){
            Array(xLen) {i -> space}
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



    fun parsePass2(line: String): List<Action> {
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

    private fun parsePasswordLine(line: String): List<Action> {
        val scanner = Scanner(line.trim())

        val anyCharPattern = ".+"
        val directionPattern = "[LR]"

        return sequence<Action> {
            while (scanner.hasNext(anyCharPattern)) {
                if (scanner.hasNext(directionPattern)) {
                    when (val next = scanner.next(directionPattern)) {
                        "L" -> yield(Rotate(Rotate.Direction.LEFT))
                        "R" -> yield(Rotate(Rotate.Direction.RIGHT))
                        else -> throw Exception("Only L or R expected but found = '$next'")
                    }
                } else if (scanner.hasNextInt()) {
                    yield(MoveForward(scanner.nextInt()))
                } else {
                    throw Exception("Scanner state = ${scanner}")
                    break
                }
            }
        }.toList()
    }

    data class Point(val x:Int, val y:Int, val type:Type) {
        enum class Type {
            TILE,
            WALL
        }
    }


    data class Player(var x:Int, var y:Int, var heading:Heading = Heading.RIGHT ) {
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

    open class Action
    data class MoveForward(val steps: Int):Action()
    data class Rotate(val direction: Direction):Action() {
        enum class Direction {
            LEFT,
            RIGHT;
        }
    }

}