package tasks.day11

import base.Task


class MonkeyInTheMiddle : Task {
    override val id: Int
        get() = 11


    override fun part1(lines: Sequence<String>): Long {
        val monkeys = toMonkeysList(lines)

        val worryLevelFun: (Monkey, Long) -> Long = { monkey, item ->
            monkey.operation(item) / 3
        }

        val times = 20L
        val (a, b) = calculateMonkeyMoves(monkeys, times, worryLevelFun)

        return (a * b)
    }

    override fun part2(lines: Sequence<String>): Long {
        val monkeys = toMonkeysList(lines)

        val dividerProduct = monkeys.map { it.testDivider }.reduce { a, b -> a * b }
        val worryLevelFun: (Monkey, Long) -> Long = { monkey, item ->
            (monkey.operation(item) % dividerProduct)
        }

        val times = 10000L
        val (a, b) = calculateMonkeyMoves(monkeys, times, worryLevelFun)

        monkeys.forEach(::println)

        return a * b
    }

    private fun toMonkeysList(lines: Sequence<String>) =
        lines.joinToString("\n")
            .split("\n\n")
            .map { parseMonkey(it.split("\n")) }
            .toList()


    private fun calculateMonkeyMoves(
        monkeys: List<Monkey>,
        repeatNum: Long,
        worryLevel: (Monkey, Long) -> Long
    ): List<Long> {
        val monkeyMap = monkeys.associateBy { it.id }

        repeat(repeatNum.toInt()) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    val worryLevelVal = worryLevel(monkey, item)
                    val destMonkey = if (worryLevelVal % monkey.testDivider == 0L) {
                        monkey.ifTrue
                    } else {
                        monkey.ifFalse
                    }
                    monkey.counter += 1
                    monkeyMap[destMonkey]!!.items.add(worryLevelVal)
                }
                monkey.items.clear()
            }
        }
        return monkeys.map { it.counter }.sortedDescending().toList()
    }


    private fun parseMonkey(monkeyLines: List<String>): Monkey {
        val id = monkeyLines[0].replace("\\D+".toRegex(), "").toLong()
        val items = monkeyLines[1]
            .replace("[^0-9,]+".toRegex(), "")
            .split(",")
            .map { it.toLong() }.toMutableList()

        val opNumber = monkeyLines[2].replace("\\D+".toRegex(), "").toLongOrNull()
        val operation = if (monkeyLines[2].contains("*")) {
            { n: Long -> n * (opNumber ?: n) }
        } else if (monkeyLines[2].contains("+")) {
            { n: Long -> n + (opNumber ?: n) }
        } else {
            throw Exception("The operation line doesn't contain '+' nor '*' sign! Line: '${monkeyLines[2]}' ")
        }

        val divider = monkeyLines[3].replace("\\D+".toRegex(), "").toLong()
        val ifTrue = monkeyLines[4].replace("\\D+".toRegex(), "").toLong()
        val ifFalse = monkeyLines[5].replace("\\D+".toRegex(), "").toLong()

        return Monkey(
            id,
            items,
            operation,
            divider,
            ifTrue,
            ifFalse
        )
    }


    private data class Monkey(
        val id: Long,
        val items: MutableList<Long> = mutableListOf(),
        val operation: (Long) -> Long,
        val testDivider: Long,
        val ifTrue: Long,
        val ifFalse: Long,
        var counter: Long = 0
    )
}

