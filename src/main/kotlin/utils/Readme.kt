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
        val txt = template + "\n" + (1..allTasks.maxOf { it.id })
            .asSequence()
            .sortedDescending()
            .map { id ->
                val task = allTasks.firstOrNull { it.id == id }
                val (part1, part2) = if(task!=null) testAssertions(task, false) else false to false
                fun star(condition: Boolean?) = if (condition == true) "✰" else "★"
                "Day ${id.toString().padStart(2, '0')} |     ${star(part1)}    |     ${star(part2)}   "
            }.joinToString(separator = " \n") + "\n" + "\n" +
                "Legend: ★ - not finished, ✰ - finished"

        return txt + "\n"  +
                "\nI've got ${txt.count { it == '✰' } - 1} stars total!"
    }
}