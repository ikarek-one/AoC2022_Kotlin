package tasks.day05

import base.Task
import utils.testAssertions

class StackedCrates : Task {
    override val id: Int
        get() = 5

    override fun part1(lines: Sequence<String>): Long {
        return solve(lines, 1)
    }

    override fun part2(lines: Sequence<String>): Long {
        return solve(lines, 2)
    }


    private fun solve(
        lines: Sequence<String>,
        partNumber: Int
    ): Long {
        val (stacksText, movesText) = lines
            .joinToString("\n")
            .split("\n\n")

        val stacks = parseStacks(stacksText)
        val moves = parseMoves(movesText)

        when (partNumber) {
            1 -> doMoves(stacks, moves)
            2 -> doManyMovesAtTime(stacks, moves)
            else -> throw Exception("Unexpected part number, found = $partNumber when only 1 or 2 were expected")
        }

        val solution = stacks.keys.toList().sorted()
            .asSequence()
            .map { stacks[it]!!.first() }
            .joinToString(separator = "")

        println("Day 5, part $partNumber solution is: '$solution'")

        // only hash code is checked for tests
        return solution.hashCode().toLong()
    }

    private fun parseStacks(stacksText: String): MutableMap<Int, ArrayDeque<Char>> {
        val lines = stacksText.split("\n")
        val numbersIdx = lines.indexOfFirst { it.contains("1") }
        val numbersLine = lines[numbersIdx]

        val stackMap = mutableMapOf<Int, ArrayDeque<Char>>()

        numbersLine
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .forEach { stackMap[it] = ArrayDeque() }

        for (i in numbersIdx - 1 downTo 0) {
            val line = lines[i]
            for (k in line.indices) {
                val ch = line[k]
                if (ch.isLetter()) {
                    val stackNumber = numbersLine[k].toString().toInt()
                    stackMap[stackNumber]!!.addFirst(ch)
                }
            }
        }

        return stackMap
    }

    private fun parseMoves(moveText: String): List<Move> {
        return moveText.split("\n")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { line ->
                val (number, from, to) =
                    line.split("\\D+".toRegex())
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }

                Move(number, from, to)
            }.toList()
    }

    private fun doMoves(stacks: Map<Int, ArrayDeque<Char>>, moves: List<Move>) {
        for (move in moves) {
            repeat(move.howMany) {
                val crate = stacks[move.from]!!.removeFirst()
                stacks[move.to]!!.addFirst(crate)
            }
        }
    }

    private fun doManyMovesAtTime(stacks: Map<Int, ArrayDeque<Char>>, moves: List<Move>) {
        for (move in moves) {
            val crates = mutableListOf<Char>()

            repeat(move.howMany) {
                val crate = stacks[move.from]!!.removeFirst()
                crates.add(crate)
            }

            for (crate in crates.reversed()) {
                stacks[move.to]!!.addFirst(crate)
            }

        }
    }

    private data class Move(val howMany: Int, val from: Int, val to: Int)

}

fun main() {
    testAssertions(StackedCrates())
}