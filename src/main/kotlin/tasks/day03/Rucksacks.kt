package tasks.day03

import base.Task
import utils.SequenceUtils.splitSequence

class Rucksacks: Task {
    override val id: Int
        get() = 3


    override fun part1(lines: Sequence<String>): Long {
            return lines
                .filter { it.isNotBlank() }
                .map { oneCommonElement(it) }
                .map { priority(it) }
                .sum()
    }

    override fun part2(lines: Sequence<String>): Long {
        return splitSequence(lines.filter { it.isNotBlank() }, groupSize = 3)
            .map { list -> badge(list) }
            .map { ch -> priority(ch) }
            .sum()
    }


    private fun badge(threeLines: List<String>): Char {
        if (threeLines.size != 3) {
            throw Exception("Three elements expected but found a list = ${threeLines} of ${threeLines.size} elements!")
        }

        val first = threeLines[0].toCharArray().toSet()
        val second = threeLines[1].toCharArray().toSet()
        val third = threeLines[2].toCharArray().toSet()

        val oneElemSet = first.intersect(second).intersect(third)

        if (oneElemSet.size != 1) {
            throw Exception("Only one element in the set was expected but found set = ${oneElemSet}!")
        }

        return oneElemSet.first()
    }

    private fun oneCommonElement(line: String): Char {
        val halfLen = line.length / 2

        val first = line.toCharArray(0, halfLen).toSet()
        val second = line.toCharArray(halfLen).toSet()

        val oneElemSet = first.intersect(second)

        if (oneElemSet.size != 1) {
            throw Exception("Only one element in the set was expected but found set = ${oneElemSet}!")
        }

        return oneElemSet.first()
    }

    private fun priority(ch: Char): Long {
        return when (ch) {
            in 'a'..'z' -> (ch - 'a' + 1)
            in 'A'..'Z' -> (ch - 'A' + 27)
            else -> throw Exception("Only chars in range a-z or A-Z were expected but found = '$ch'")
        }.toLong()
    }
}