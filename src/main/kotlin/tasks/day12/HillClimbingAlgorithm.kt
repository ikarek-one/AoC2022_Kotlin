package tasks.day12

import base.Task
import tasks.day12.BreadthFirstSearch.bfs
import tasks.day12.BreadthFirstSearch.getPath



class HillClimbingAlgorithm : Task {
    override val id: Int
        get() = 12


    override fun part1(lines: Sequence<String>): Long {
        val grid = parseGrid(lines)
        val flattenedGrid = grid.flatten()

        val neighbourMap = flattenedGrid.asSequence().associateWith { neighbours(it, grid) }


        val start = flattenedGrid.first { it.type == GridPoint.Type.START }
        val end = flattenedGrid.first { it.type == GridPoint.Type.END }

        val bfsParentMap = bfs(flattenedGrid, neighbourMap, start)

        val path = getPath(start, end, bfsParentMap)

        return path.size.toLong() - 1
    }


    override fun part2(lines: Sequence<String>): Long {
        val text = lines.joinToString(separator = "\n").replace('S', 'a')

        val paths = text.withIndex().filter { it.value == 'a' }.map { it.index }.map { idx ->
                val buffer = StringBuffer(text)
                buffer.setCharAt(idx, 'S')
                buffer.toString()
            }.map { HillClimbingAlgorithm().part1(it.lineSequence()) }

        return paths.filter { it > 0 }.min()
    }


    private fun neighbours(point: GridPoint, grid: List<List<GridPoint>>): List<GridPoint> {
        return transpositions.map { it.first + point.x to it.second + point.y }
            .mapNotNull { grid.getOrNull(it.second)?.getOrNull(it.first) }.filter { point.canGoToNext(it) }
    }

    private fun parseGrid(lines: Sequence<String>): List<List<GridPoint>> {
        return lines.withIndex().map { (row, line) ->
                sequence {
                    for (i in line.indices) {
                        val point = when (line[i]) {
                            'S' -> GridPoint(i, row, startElevation, GridPoint.Type.START)
                            'E' -> GridPoint(i, row, endElevation, GridPoint.Type.END)
                            in 'a'..'z' -> GridPoint(i, row, line[i], GridPoint.Type.MID)
                            else -> throw Exception("Only [a-zES] chars expected, but found '${line[i]}' in (x,y) = ($i,$row)!")
                        }
                        yield(point)
                    }
                }.toList()
            }.toList().also {
                //checking if the list has only one start and one end
                    grid ->
                val excMsg = "The list has more than one element of type: "
                grid.flatten().singleOrNull { it.type == GridPoint.Type.START } ?: throw Exception(excMsg + "start")
                grid.flatten().singleOrNull { it.type == GridPoint.Type.END } ?: throw Exception(excMsg + "end")
            }
    }

    private data class GridPoint(val x: Int, val y: Int, val label: Char, val type: Type = Type.MID) {
        fun canGoToNext(next: GridPoint): Boolean {
            return this != next && this.type != Type.END && (next.label.code - this.label.code) <= 1
        }

        enum class Type {
            START, END, MID
        }
    }

    private companion object {
        const val startElevation = 'a'
        const val endElevation = 'z'

        val transpositions = listOf(
            0 to -1,
            -1 to 0,
            1 to 0,
            0 to 1,
        )
    }

}


