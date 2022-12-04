package tasks.day04

import base.Task


class Overlapping : Task {
    override val id: Int
        get() = 4

    override fun part1(lines: Sequence<String>): Long {
        return parseSequence(lines)
            .count { areFullyOverlapping(it) }
            .toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        return parseSequence(lines)
            .count { areOverlapping(it.first, it.second) }
            .toLong()
    }

    private fun parseSequence(lines: Sequence<String>) = lines
        .filter { it.isNotBlank() }
        .map { parseLine(it) }

    private fun parseLine(line: String): Pair<IntRange, IntRange> {
        val tokens = line.split(",")
            .map { it.split("-").map { i -> i.toInt() } }
            .map { x -> toRange(x[0], x[1]) }
            .toList()

        return tokens[0] to tokens[1]
    }

    private fun toRange(a: Int, b: Int): IntRange {
        return if (a <= b) a..b else b..a
    }

    private fun areFullyOverlapping(pair: Pair<IntRange, IntRange>): Boolean {
        val (r1, r2) = pair
        return isFullyOverlapping(r1, r2) || isFullyOverlapping(r2, r1)
    }

    private fun isFullyOverlapping(r1: IntRange, r2: IntRange): Boolean {
        // r2 is expected to be the smaller one, included in r1
        return r1.first <= r2.first && r2.first <= r1.last
                && r1.first <= r2.last && r2.last <= r1.last
    }

    private fun areOverlapping(r1: IntRange, r2: IntRange): Boolean {
        if (r1.first == r2.first) return true

        // f - first, s - second
        val (f, s) = if (r1.first < r2.first) r1 to r2 else r2 to r1
        return f.last >= s.first
    }
}

