package tasks.day21

import base.Task
import utils.fromConsolePartTwo
import utils.testAssertions


fun main() {
    fromConsolePartTwo(MonkeyMath())
//    testAssertions(::MonkeyMath)
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

    // part two passes the test but doesn't pass the real input - grateful for any hints why!
    override fun part2(lines: Sequence<String>): Long {
        val map = mutableMapOf<String, MonkeyVal>()

        lines.filter { it.isNotBlank() }
            .forEach {
                parsePartTwo(it, map)
            }

//        println("textAnswer = '${map["root"]?.textAnswer(map)}' \n")
//        println("revTextAnswer = '${map["root"]?.reversedTextAnswer(map)}' \n")

        val root = map["root"]!!
        val (left, right) = root.monkey1 to root.monkey2

        if (map[left]!!.isHumanInSubtree(map)) {
            val seed = map[right]!!.answer(map)
            return solve(map[left]!!, map, seed)
        } else {
            map[MonkeyVal.humanName] = MonkeyVal(MonkeyVal.humanName, map[left]!!.answer(map))
            val seed = map[left]!!.answer(map)
            return solve(map[right]!!, map, seed)
        }
    }


    fun solve(root: MonkeyVal, map: MutableMap<String, MonkeyVal>, seed: Long): Long {
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
            return solve(left, map, newSeed)
        } else {
            val newSeed = root.operation!!.reversed().function(
                seed,
                left.answer(map)
            )
            return solve(right, map, newSeed)
        }
    }

    fun parseLine(line: String, map: MutableMap<String, () -> Long>) {
        val numberRegex = "\\s?[+-]?\\d+\\s?".toRegex()

        val key = line.substringBefore(":")
        val valuePart = line.substringAfter(":")

        val function = if (valuePart.matches(numberRegex)) {
            { -> valuePart.replace("\\s+".toRegex(), "").toLong() }
        } else {
//            println("valPart = '$valuePart'")
            val (monkeyOne, operand, monkeyTwo) = valuePart.split("\\s+".toRegex()).filter { it.isNotBlank() }
            when (operand) {
                "+" -> { -> map[monkeyOne]!!() + map[monkeyTwo]!!() }
                "-" -> { -> map[monkeyOne]!!() - map[monkeyTwo]!!() }
                "/" -> { -> map[monkeyOne]!!() / map[monkeyTwo]!!() }
                "*" -> { -> map[monkeyOne]!!() * map[monkeyTwo]!!() }
                else -> throw Exception("Unexpected operand found: '$operand'")
            }
        }

        map.put(key, function)
    }

    fun parsePartTwo(line: String, map: MutableMap<String, MonkeyVal>) {
        val numberRegex = "\\s?[+-]?\\d+\\s?".toRegex()

        val key = line.substringBefore(":")
        val valuePart = line.substringAfter(":")

        val monkeyVal = if (valuePart.matches(numberRegex)) {
            MonkeyVal(key, valuePart.replace("\\s+".toRegex(), "").toLong())
        } else {
            val (monkeyOne, operand, monkeyTwo) = valuePart.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val operation = when (operand) {
                "+" -> Operation.ADDITION
                "-" -> Operation.SUBSTRACTION
                "*" -> Operation.MULTIPLICATION
                "/" -> Operation.DIVISION
                else -> throw Exception("Unexpected operand found: '$operand'")
            }

            MonkeyVal(key, monkeyOne, monkeyTwo, operation)
        }

        map.put(key, monkeyVal)
    }


    class MonkeyVal private constructor(
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
            val humanName = "humn"
        }

        fun left(map: MutableMap<String, MonkeyVal>) = map[monkey1]
        fun right(map: MutableMap<String, MonkeyVal>) = map[monkey2]

        fun isHumanInSubtree(map: MutableMap<String, MonkeyVal>): Boolean {
            if (this.name == humanName) {
                return true
            } else if (this.number != null) {
                return false
            } else {
                return left(map)!!.isHumanInSubtree(map) || right(map)!!.isHumanInSubtree(map)
            }
        }

        fun answer(map: MutableMap<String, MonkeyVal>): Long {
            return if (number != null) {
                number
            } else {
                operation!!.function(map[monkey1]!!.answer(map), map[monkey2]!!.answer(map))
            }
        }

//        fun reversedAnswer(map: MutableMap<String, MonkeyVal>): Long {
//            return if (number != null) {
//                number
//            } else {
//                operation!!.reversed().function(map[monkey1]!!.answer(map), map[monkey2]!!.answer(map))
//            }
//        }
//
//
//        fun textAnswer(map: MutableMap<String, MonkeyVal>): String {
//            return if (name == humanName) {
//                humanName
//            } else if (number != null) {
//                number.toString()
//            } else {
//                "(" + map[monkey1]!!.textAnswer(map) + operation!!.symbol + map[monkey2]!!.textAnswer(map) + ")"
//            }
//        }
//
//        fun reversedTextAnswer(map: MutableMap<String, MonkeyVal>): String {
//            return if (name == humanName) {
//                humanName
//            } else if (number != null) {
//                number.toString()
//            } else {
//                "(" + map[monkey1]!!.reversedTextAnswer(map) + operation!!.reversed().symbol + map[monkey2]!!.reversedTextAnswer(
//                    map
//                ) + ")"
//            }
//        }


    }

    enum class Operation(val symbol: Char, val function: (Long, Long) -> Long) {
        ADDITION('+', { a, b -> a + b }),
        SUBSTRACTION('-', { a, b -> a - b }),
        MULTIPLICATION('*', { a, b -> a * b }),
        DIVISION('/', { a, b -> a / b });

        fun reversed(): Operation {
            return when (this) {
                ADDITION -> SUBSTRACTION
                SUBSTRACTION -> ADDITION
                MULTIPLICATION -> DIVISION
                DIVISION -> MULTIPLICATION
            }
        }
    }
}