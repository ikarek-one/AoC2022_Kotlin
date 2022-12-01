package base

interface Task {
    val id: Int

    fun part1(lines: Sequence<String>): Long? = null
    fun part2(lines: Sequence<String>): Long? = null
}