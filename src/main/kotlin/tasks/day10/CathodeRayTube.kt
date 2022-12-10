package tasks.day10

import base.Task
import utils.testAssertions

class CathodeRayTube : Task {
    override val id: Int
        get() = 10

    override fun part1(lines: Sequence<String>): Long {
        val stateMap = cycleToValueMap(lines)

        val checkedCycles = listOf(20, 60, 100, 140, 180, 220)
        return checkedCycles.asSequence()
            .map { i -> stateMap[i]!! * i }
            .sum()
            .toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        val stateMap = cycleToValueMap(lines)

        val pixels = sequence {
            for (i in 0..239) {
                val sprite = stateMap[i + 1]!! + (i / 40) * 40
                val pixel = if (i in (sprite - 1)..(sprite + 1)) "#" else "."
                yield(pixel)
            }
        }

        val grid = pixels.chunked(40)
            .map { it.joinToString(separator = "") }
            .joinToString(separator = "\n")

        println(grid)

        // the answer is PAPJCBHP but only the hash code of the grid is checked
        return grid.hashCode().toLong()
    }

    private fun cycleToValueMap(lines: Sequence<String>): Map<Int, Int> {
        return sequence {
            for (line in lines.filter { it.isNotBlank() }) {
                if (line == "noop") yield(0)
                else {
                    assert(line.startsWith("addx "))
                    yield(0)
                    yield(line.replace("[^0-9-]+".toRegex(), "").toInt())
                }
            }
        }.runningFold(1) { acc: Int, i: Int -> acc + i }
            .withIndex()
            .associate { idxVal -> idxVal.index + 1 to idxVal.value }
    }

}


fun main() {
    testAssertions(CathodeRayTube())
}



// the function below works correctly but I decided to make another solution, perhaps a more elegant one


//private fun cycleToValueMap(lines: Sequence<String>): Map<Int, Int> {
//    val lineList = lines.filter { it.isNotBlank() }.toList()
//    var x = 1
//    var cycle = 0
//    val stateMap = mutableMapOf<Int, Int>()
//
//    for (i in lineList.indices) {
//        val cmd = lineList[i]
//        if (cmd == "noop") {
//            cycle += 1
//            stateMap.put(cycle, x)
//        } else if (cmd.startsWith("addx")) {
//            val factor = cmd.split("\\s+".toRegex())[1].toInt()
//            cycle += 1
//            stateMap.put(cycle, x)
//            cycle += 1
//            stateMap.put(cycle, x)
//            x += factor
//        } else {
//            throw Exception()
//        }
//
//        //            stateMap.put(cycle, x)
//    }
//    return stateMap
//}
