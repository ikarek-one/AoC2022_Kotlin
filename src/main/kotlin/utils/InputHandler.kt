package utils

import base.Task
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

fun fromConsole(): Sequence<String> =
    generateSequence { readLine() }

fun fromFileAsString(id: Int): String {
    val filename = "src/main/resources/inputs/input${id}.txt"
    val file = File(filename)
    return try {
        file.readText().replace("\r".toRegex(), "")
    } catch (exc: FileNotFoundException) {
        ""
    }
}

fun fromFile(id: Int): Sequence<String> {
    return fromFileAsString(id)
        .split("\n".toRegex())
        .asSequence()
}

fun answerFromFile(id: Int): List<Long?> {
    val filename = "src/main/resources/answers/answer${id}.txt"
    val file = File(filename)
    val text = try {
        file.readText().trim().replace("\r".toRegex(), "")
    } catch (ioe: IOException) {
        ""
    }

    return text.split("\n".toRegex()).map { it.trim().toLongOrNull() }
}

fun answerPartOne(id: Int): Long? {
    val list = answerFromFile(id)
    return if (list.size >= 1)
        list[0]
    else
        null
}

fun answerPartTwo(id: Int): Long? {
    val list = answerFromFile(id)
    return if (list.size >= 2)
        list[1]
    else
        null
}

fun fromConsolePartOne(task: Task) {
    println("part one = ${task.part1(fromConsole())}")
}

fun fromConsolePartTwo(task: Task) {
    println("part two = ${task.part2(fromConsole())}")
}