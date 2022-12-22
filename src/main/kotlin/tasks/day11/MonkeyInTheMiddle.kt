package tasks.day11

import base.Task
import utils.testAssertions


class MonkeyInTheMiddle : Task {
    override val id: Int
        get() = 11

    override fun part1(lines: Sequence<String>): Long {
        val monkeys = lines.joinToString("\n")
            .split("\n\n")
            .map { parseMonkey(it.split("\n")) }
            .toList()

        val times = 20
        val (a, b) = calculateMonkeyMoves(monkeys, times)

        return (a * b).toLong()
    }


    private fun calculateMonkeyMoves(monkeys: List<Monkey>, repeatNum: Int): List<Int> {
        val monkeyMap = monkeys.associateBy { it.id }

        repeat(repeatNum) {
            for (monkey in monkeys) {
                for (item in monkey.items) {
                    val worryLevel = monkey.worryLevel(item)
                    val destMonkey = if (worryLevel % monkey.testDivider == 0) {
                        monkey.ifTrue
                    } else {
                        monkey.ifFalse
                    }
                    monkey.counter += 1
                    monkeyMap[destMonkey]!!.items.add(worryLevel)
                }
                monkey.items.clear()
            }
        }


        return monkeys.map { it.counter }.sortedDescending().toList()
    }


    private fun parseMonkey(monkeyLines: List<String>): Monkey {
        val id = monkeyLines[0].replace("\\D+".toRegex(), "").toInt()
        val items = monkeyLines[1]
            .replace("[^0-9,]+".toRegex(), "")
            .split(",")
            .map { it.toInt() }.toMutableList()

//        println("ml2 = ${monkeyLines[2]}")
        val opNumber = monkeyLines[2].replace("\\D+".toRegex(), "").toIntOrNull()
        val operation = if (monkeyLines[2].contains("*")) {
            { n: Int -> n * (opNumber ?: n) }
        } else if (monkeyLines[2].contains("+")) {
            { n: Int -> n + (opNumber ?: n) }
        } else {
            throw Exception()
        }

        val divider = monkeyLines[3].replace("\\D+".toRegex(), "").toInt()
        val ifTrue = monkeyLines[4].replace("\\D+".toRegex(), "").toInt()
        val ifFalse = monkeyLines[5].replace("\\D+".toRegex(), "").toInt()

        return Monkey(
            id,
            items,
            operation,
            divider,
            ifTrue,
            ifFalse
        )
    }


    inner class Monkey(
        val id: Int,
        val items: MutableList<Int> = mutableListOf(),
        val operation: (Int) -> Int,
        val testDivider: Int,
        val ifTrue: Int,
        val ifFalse: Int,
        var counter: Int = 0
    ) {
        fun worryLevel(item: Int): Int {
            return (this.operation(item) / 3)
        }

    }
}

fun main() {
    testAssertions(::MonkeyInTheMiddle)
}

