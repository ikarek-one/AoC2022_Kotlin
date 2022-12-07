package tasks.day07

import base.Task
import utils.testAssertions
import java.util.*

class NoSpaceLeft : Task {
    override val id: Int
        get() = 7

    override fun part1(lines: Sequence<String>): Long {
        val contents = parseLines(lines)

        val upperLimitToTake = 100000
        return contents
            .asSequence()
            .mapNotNull { it as? Directory }
            .map { dir -> directorySize(dir) }
            .filter { n -> n <= upperLimitToTake }
            .sum()
    }

    override fun part2(lines: Sequence<String>): Long {
        val capacity = 70000000
        val required = 30000000

        val contents = parseLines(lines)
        val rootName = "/"
        val rootDir = contents.first { elem -> (elem as? Directory)?.name == rootName } as Directory

        val usedSpace = directorySize(rootDir)
        val neededSpace = required - (capacity - usedSpace)

        return contents
            .asSequence()
            .mapNotNull { it as? Directory }
            .map { directorySize(it) }
            .sorted()
            .filter { it >= neededSpace }
            .first()
    }

    private fun directorySize(dir: Directory): Long {
        return dir.content.sumOf { data ->
            when (data) {
                is File -> data.size
                is Directory ->
                    if (data.content.isEmpty()) 0 else directorySize(data)

                else -> throw Exception("Unexpected type: ${data.javaClass.name}")
            }
        }
    }

    private fun parseLines(lines: Sequence<String>): List<Data> {
        val dirStack = Stack<Directory>()
        val elements = mutableListOf<Data>()
        fun peekOrNull() = if (dirStack.isEmpty()) null else dirStack.peek()

        for (line in lines.filter { it.isNotBlank() }.map { it.trim() }) {
            when {
                line.startsWith("\$ cd ") -> {
                    val name = line.substringAfter("\$ cd ")
                    if (name.contains("..")) {
                        dirStack.pop()
                    } else {
                        val newDir = Directory(name)
                        peekOrNull()?.content?.add(newDir)
                        elements.add(newDir)
                        dirStack.push(newDir)
                    }
                }

//                ignore lines that == "$ ls"
//                ignore lines with "dir"

                line.matches("\\d+ .+".toRegex()) -> {
                    val tokens = line.split("\\s+".toRegex())
                    val size = tokens[0].toLong()
                    val name = tokens[1]
                    val newFile = File(name, size)
                    elements.add(newFile)
                    peekOrNull()?.content?.add(newFile)
                }

            }
        }

        return elements.toList()
    }


    private open class Data
    private data class Directory(val name: String, val content: MutableList<Data> = mutableListOf()) : Data()
    private data class File(val name: String, val size: Long) : Data()
}

fun main() {
    testAssertions(NoSpaceLeft())
}