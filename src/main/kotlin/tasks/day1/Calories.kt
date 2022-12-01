package tasks.day1

import base.Task
import utils.testAssertions

// day 1
class Calories : Task {
    override val id: Int
        get() = 1

    override fun part1(lines: Sequence<String>): Long {
        return summedCalories(lines).max()
    }

    override fun part2(lines: Sequence<String>): Long {
        val summingFirst = 3

        return summedCalories(lines)
            .sortedDescending()
            .take(summingFirst)
            .sum()
    }

    private fun summedCalories(lines: Sequence<String>): Sequence<Long> {
        return lines.joinToString(separator = "\n")
            .split("\n\n".toRegex())
            .asSequence()
            .map { it.split("\n".toRegex()) }
            .map { it.map { line -> line.toLongOrNull() ?: 0 }.sum() }
    }
}

fun main() {
    testAssertions(Calories())
}