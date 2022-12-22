package tasks.day21

import base.Task
import utils.testAssertions


fun main() {
    testAssertions(::MonkeyMath)
}

class MonkeyMath : Task {
    override val id: Int
        get() = 21

    override fun part1(lines: Sequence<String>): Long {
        val map = mutableMapOf<String, () -> Long>()
        val wantedMonkey = "root"

        lines.filter { it.isNotBlank() }
            .forEach {
                parseLine(it, map)
            }

        return map[wantedMonkey]!!()
    }

    override fun part2(lines: Sequence<String>): Long {
        val map = mutableMapOf<String, MonkeyVal>()

        lines.filter { it.isNotBlank() }
            .forEach {
                parsePartTwo(it, map)
            }

        val root = map["root"]!!
        val (left, right) = root.monkey1 to root.monkey2

        return if (map[left]!!.isHumanInSubtree(map)) {
            val seed = map[right]!!.answer(map)
            solvePartTwo(map[left]!!, seed, map)
        } else {
            val seed = map[left]!!.answer(map)
            solvePartTwo(map[right]!!, seed, map)
        }
    }


    private fun parseLine(line: String, map: MutableMap<String, () -> Long>) {
        val numberRegex = "\\s?[+-]?\\d+\\s?".toRegex()

        val key = line.substringBefore(":")
        val valuePart = line.substringAfter(":")

        @Suppress("RedundantLambdaArrow")
        val function = if (valuePart.matches(numberRegex)) {
            { -> valuePart.replace("\\s+".toRegex(), "").toLong() }
        } else {
            val (monkeyOne, operand, monkeyTwo) = valuePart.split("\\s+".toRegex()).filter { it.isNotBlank() }
            when (operand) {
                "+" -> { -> map[monkeyOne]!!() + map[monkeyTwo]!!() }
                "-" -> { -> map[monkeyOne]!!() - map[monkeyTwo]!!() }
                "/" -> { -> map[monkeyOne]!!() / map[monkeyTwo]!!() }
                "*" -> { -> map[monkeyOne]!!() * map[monkeyTwo]!!() }
                else -> throw Exception("Unexpected operand found: '$operand'")
            }
        }

        map[key] = function
    }

    private fun parsePartTwo(line: String, map: MutableMap<String, MonkeyVal>) {
        val numberRegex = "\\s?[+-]?\\d+\\s?".toRegex()

        val key = line.substringBefore(":")
        val valuePart = line.substringAfter(":")

        val monkeyVal = if (valuePart.matches(numberRegex)) {
            MonkeyVal(key, valuePart.replace("\\s+".toRegex(), "").toLong())
        } else {
            val (monkeyOne, operand, monkeyTwo) = valuePart.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val operation = when (operand) {
                "+" -> Operation.ADDITION
                "-" -> Operation.SUBTRACTION
                "*" -> Operation.MULTIPLICATION
                "/" -> Operation.DIVISION
                else -> throw Exception("Unexpected operand found: '$operand'")
            }

            MonkeyVal(key, monkeyOne, monkeyTwo, operation)
        }

        map[key] = monkeyVal
    }

    private fun solvePartTwo(root: MonkeyVal, seed: Long, map: MutableMap<String, MonkeyVal>): Long {
        if (root.name == MonkeyVal.humanName) {
            return seed
        }

        val left = root.left(map)!!
        val right = root.right(map)!!

        if (left.isHumanInSubtree(map)) {
            val newSeed = root.operation!!.reversed().function(
                seed,
                right.answer(map)
            )
            return solvePartTwo(left, newSeed, map)
        } else {
            val newSeed = when (root.operation!!) {
                Operation.SUBTRACTION -> left.answer(map) - seed
                Operation.DIVISION -> left.answer(map) / seed
                else -> root.operation.reversed().function(
                    seed,
                    left.answer(map)
                )
            }
            return solvePartTwo(right, newSeed, map)
        }
    }


    private class MonkeyVal private constructor(
        val name: String,
        val number: Long?,
        val monkey1: String?,
        val monkey2: String?,
        val operation: Operation?
    ) {
        constructor(name: String, num: Long) : this(name, num, null, null, null)

        constructor(name: String, monkey1: String, monkey2: String, operation: Operation) : this(
            name,
            null,
            monkey1,
            monkey2,
            operation
        )

        companion object {
            const val humanName = "humn"
        }

        fun left(map: MutableMap<String, MonkeyVal>) = map[monkey1]
        fun right(map: MutableMap<String, MonkeyVal>) = map[monkey2]

        fun isHumanInSubtree(map: MutableMap<String, MonkeyVal>): Boolean {
            return if (this.name == humanName) {
                true
            } else if (this.number != null) {
                false
            } else {
                left(map)!!.isHumanInSubtree(map) || right(map)!!.isHumanInSubtree(map)
            }
        }

        fun answer(map: MutableMap<String, MonkeyVal>): Long {
            return number
                ?: operation!!.function(map[monkey1]!!.answer(map), map[monkey2]!!.answer(map))
        }

    }

    private enum class Operation(val function: (Long, Long) -> Long) {
        ADDITION({ a, b -> a + b }),
        SUBTRACTION({ a, b -> a - b }),
        MULTIPLICATION({ a, b -> a * b }),
        DIVISION({ a, b -> a / b });

        fun reversed(): Operation {
            return when (this) {
                ADDITION -> SUBTRACTION
                SUBTRACTION -> ADDITION
                MULTIPLICATION -> DIVISION
                DIVISION -> MULTIPLICATION
            }
        }
    }
}
