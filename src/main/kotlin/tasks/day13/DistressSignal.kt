package tasks.day13

import base.Task
import com.google.gson.Gson
import utils.testAssertions
import kotlin.math.min


class DistressSignal : Task {
    override val id: Int
        get() = 13

    override fun part1(lines: Sequence<String>): Long {
        return pairOfElemsSeq(lines)
            .map { pair -> pair.first.compareTo(pair.second) }
            .withIndex()
            .filter { it.value > 0 }
            .sumOf { idxVal -> idxVal.index + 1 }
            .toLong()
    }

    override fun part2(lines: Sequence<String>): Long {
        val sortedPackets = pairOfElemsSeq(lines)
            .flatMap { pair -> sequenceOf(pair.first, pair.second) }
            .toMutableList()

        sortedPackets.add(DsList(DsList(DsInteger(2))))
        sortedPackets.add(DsList(DsList(DsInteger(6))))
        sortedPackets.sortDescending()


        val idx1 = sortedPackets.indexOfLast { it == DsList(DsList(DsInteger(2))) }
        val idx2 = sortedPackets.indexOfLast { it == DsList(DsList(DsInteger(6))) }

        return ((idx1 + 1) * (idx2 + 1)).toLong()
    }

    private fun pairOfElemsSeq(lines: Sequence<String>) = lines.joinToString("\n")
        .split("\n\n")
        .asSequence()
        .map(::toPairOfElems)

    private fun toPairOfElems(doubleLine: String): Pair<DsList, DsList> {
        val (s1, s2) = doubleLine.split("\n")
        return parseLine(s1) to parseLine(s2)
    }

    private fun parseLine(line: String): DsList {
        val gson = Gson()
        val json = gson.fromJson(line, mutableListOf<Any>().javaClass)


        val list = json.asSequence()
            .map { obj ->
                when (obj) {
                    is Double -> DsInteger(obj)
                    is List<*> -> parseLine(gson.toJson(obj))
                    else -> throw Exception("Incompatible type: ${obj.javaClass.name}")
                }
            }
            .toList()

        return DsList(list)
    }


    private abstract class DsObject : Comparable<DsObject>

    private data class DsInteger(val value: Int) : DsObject() {
        constructor(doubleVal: Double) : this(doubleVal.toInt())

        override fun compareTo(right: DsObject): Int {
            return when (right) {
                is DsInteger -> right.value - this.value
                is DsList -> DsList(listOf(this)).compareTo(right)
                else -> throw Exception("Incompatible type: ${right.javaClass.name}")
            }
        }
    }

    private data class DsList(val list: List<DsObject>) : DsObject() {
        constructor(dsObject: DsObject) : this(listOf(dsObject))

        override fun compareTo(right: DsObject): Int {
            return when (right) {
                is DsInteger -> this.compareTo(DsList(listOf(right)))
                is DsList -> {
                    for (idx in 0 until min(this.list.size, right.list.size)) {
                        val comparison = this.list[idx].compareTo(right.list[idx])
                        if (comparison != 0) return comparison
                    }
                    return right.list.size - this.list.size
                }

                else -> throw Exception("Incompatible type: ${right.javaClass.name}")
            }
        }
    }
}


fun main() {
    testAssertions(::DistressSignal)

}

