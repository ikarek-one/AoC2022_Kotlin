package tasks.day07

import base.Task
import utils.testAssertions
import java.util.*

class NoSpaceLeft : Task {
    override val id: Int
        get() = 7

    override fun part1(lines: Sequence<String>): Long {
        val elems = parseLines(lines)

        elems
            .mapNotNull { it as? Directory }
            .map { dir -> "name = ${dir.name}, size = ${calcSize(dir)}" }
            .forEach(::println)


        return elems
            .mapNotNull { it as? Directory }
            .map { dir -> calcSize(dir) }
            .filter { n -> n <= 100000 }
            .sum()
    }

    override fun part2(lines: Sequence<String>): Long {
        val total = 70000000
        val req = 30000000

        val elems = parseLines(lines)
        val rootname = "/"
        val rootdir = elems.first { elem -> (elem as? Directory)?.name == rootname } as Directory

        val totalSize = calcSize(rootdir)
        val neededSize = req - (total - totalSize)
        println("neededSize = $neededSize")

        return elems
            .mapNotNull { it as? Directory }
            .map { calcSize(it) }
            .sorted()
            .filter { it >= neededSize }
            .first()
    }

    fun calcSize(dir: Directory): Long {
        return sequence<Long> {
            for (data in dir.content) {
                val size = when (data) {
                    is File -> data.size
                    is Directory ->
                        if (data.content.isEmpty())
                            0
                        else
                            calcSize(data)

                    else -> throw Exception("Unexpected type: ${data.javaClass.name}")
                }
                yield(size)
            }
        }.sum()
    }

    fun parseLines(lines: Sequence<String>): MutableList<Element> {
        val dirStack = Stack<Directory>()
        val elements = mutableListOf<Element>()
        fun peekOrNull() = if (dirStack.isEmpty()) null else dirStack.peek()

        for (line in lines.filter { it.isNotBlank() }.map { it.trim() }) {
            when {
                line.startsWith("\$ cd ") -> {
                    val name = line.substringAfter("\$ cd ")
                    if (name.contains("..")) {
                        elements.add(ChangeDirectory(".."))
                        dirStack.pop()
                    } else {
                        val newDir = Directory(name, peekOrNull())
                        peekOrNull()?.content?.add(newDir)
                        elements.add(newDir)
                        dirStack.push(newDir)
                    }
                }

                line == "\$ ls" -> elements.add(ListCmd(peekOrNull()?.name!!))

                line.startsWith("dir ") -> {
                    val name = line.substringAfter("dir ")
                    val newDir = Directory(name, peekOrNull())
                    elements.add(newDir)
                    peekOrNull()?.content?.add(newDir)
                }

                line.matches("\\d+ .+".toRegex()) -> {
                    val tokens = line.split("\\s+".toRegex())
                    val size = tokens[0].toLong()
                    val name = tokens[1]
                    val newFile = File(name, size, peekOrNull()!!)
                    elements.add(newFile)
                    peekOrNull()?.content?.add(newFile)
                }
            }
        }

        return elements
    }


}

fun main() {
    testAssertions(NoSpaceLeft())
}

open class Element

open class Command : Element()

data class ChangeDirectory(val dir: String) : Command()
data class ListCmd(val dir: String) : Command()

open class Data : Element()
data class Directory(val name: String, val root: Directory? = null, val content: MutableList<Data> = mutableListOf()) :
    Data()

data class File(val name: String, val size: Long, val root: Directory) : Data()