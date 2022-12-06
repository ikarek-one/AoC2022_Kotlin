package utils

import java.io.File

object Readme {
    val template = """
        Advent of Code 2022 - finished tasks so far:
         
        Day No | part one | part two 
    """.trimIndent()


    @JvmStatic
    fun main(args: Array<String>) {
        updateReadme()
        println(generateText())
    }

    fun updateReadme() {
        val path = "README.MD"
        File(path).writeText(generateText())
    }

    fun generateText(): String {
        return template + "\n" + allTasks
            .asSequence()
            .sortedByDescending { it.id }
            .map { task ->
                val (part1, part2) = testAssertions(task, false)
                fun star(condition: Boolean?) = if (condition == true) "✰" else "★"
                "Day ${task.id.toString().padStart(2, '0')} |     ${star(part1)}    |     ${star(part2)}   "
            }.joinToString(separator = " \n") + "\n" + "\n" +
                "Legend: ★ - not finished, ✰ - finished"
    }
}