package tasks.day08

import base.Task
import utils.testAssertions

class TreetopHouse : Task {
    override val id: Int
        get() = 8

    override fun part1(lines: Sequence<String>): Long {
        val board = parseBoard(lines)

        return sequence {
            for (x in 0 until board.size) {
                for (y in 0 until board[x].size) {
                    yield(isVisible(x, y, board))
                }
            }
        }.count { it }.toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        val board = parseBoard(lines)

        return sequence {
            for (x in board.indices) {
                for (y in board[x].indices) {
                    yield(scenicScore(x, y, board))
                }
            }
        }.max().toLong()

    }

    private fun parseBoard(lines: Sequence<String>): List<List<Int>> {
        return lines.filter { it.isNotBlank() }.map { it.toCharArray().map { ch -> ch.toString().toInt() }.toList() }
            .toList()
    }

    private fun isVisible(x: Int, y: Int, board: List<List<Int>>): Boolean {
        return goDown(x, y, board) || goUp(x, y, board) || goLeft(x, y, board) || goRight(x, y, board)
    }

    private fun scenicScore(x: Int, y: Int, board: List<List<Int>>): Int {
        return viewUp(x, y, board) * viewDown(x, y, board) * viewLeft(x, y, board) * viewRight(x, y, board)
    }

    private fun goUp(myX: Int, myY: Int, board: List<List<Int>>): Boolean {
        for (y in myY - 1 downTo 0) {
            if (board[myX][y] >= board[myX][myY]) {
                return false
            }
        }
        return true
    }

    private fun goDown(myX: Int, myY: Int, board: List<List<Int>>): Boolean {
        for (y in myY + 1 until board[myX].size) {
            if (board[myX][y] >= board[myX][myY]) {
                return false
            }
        }
        return true
    }

    private fun goLeft(myX: Int, myY: Int, board: List<List<Int>>): Boolean {
        for (x in myX - 1 downTo 0) {
            if (board[x][myY] >= board[myX][myY]) {
                return false
            }
        }
        return true
    }

    private fun goRight(myX: Int, myY: Int, board: List<List<Int>>): Boolean {
        for (x in myX + 1 until board.size) {
            if (board[x][myY] >= board[myX][myY]) {
                return false
            }
        }
        return true
    }

    private fun viewUp(myX: Int, myY: Int, board: List<List<Int>>): Int {
        var sum = 0
        for (y in myY - 1 downTo 0) {
            sum += 1
            if (board[myX][y] >= board[myX][myY]) {
                return sum
            }
        }
        return sum
    }

    private fun viewDown(myX: Int, myY: Int, board: List<List<Int>>): Int {
        var sum = 0
        for (y in myY + 1 until board[myX].size) {
            sum += 1
            if (board[myX][y] >= board[myX][myY]) {
                return sum
            }
        }
        return sum
    }

    private fun viewLeft(myX: Int, myY: Int, board: List<List<Int>>): Int {
        var sum = 0
        for (x in myX - 1 downTo 0) {
            sum += 1
            if (board[x][myY] >= board[myX][myY]) {
                return sum
            }
        }
        return sum
    }

    private fun viewRight(myX: Int, myY: Int, board: List<List<Int>>): Int {
        var sum = 0
        for (x in myX + 1 until board.size) {
            sum += 1
            if (board[x][myY] >= board[myX][myY]) {
                return sum
            }
        }
        return sum
    }

}

fun main() {
    testAssertions(TreetopHouse())
}
