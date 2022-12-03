package utils

import base.Task
import tasks.day01.Calories

val allTasks = listOf<Task>(
    // day 1
    Calories(),

    // day 2

)

fun testAll() {
    allTasks.forEach(::testAssertions)
}

fun main() {
    testAll()
}