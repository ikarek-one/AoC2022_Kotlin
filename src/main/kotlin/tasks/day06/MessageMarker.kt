package tasks.day06

import base.Task


class MessageMarker : Task {
    override val id: Int
        get() = 6

    override fun part1(lines: Sequence<String>): Long {
        val line = lines.filter { it.isNotBlank() }.first()
        return marker(line, 4)?.toLong() ?: -2
    }

    override fun part2(lines: Sequence<String>): Long {
        val line = lines.filter { it.isNotBlank() }.first()
        return marker(line, 14)?.toLong() ?: -2
    }

    private fun marker(text: String, n: Int): Int? {
        val set = text.substring(0, n).toSet()
        return if (set.size == n) n
        else if (text.isEmpty()) null
        else marker(text.substring(1), n)?.plus(1)
    }

}



