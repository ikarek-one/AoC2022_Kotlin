package utils

import base.Task

@Deprecated(message = "Use the call with a Task generating function to avoid problems in part 2 testing!")
fun testAssertions(task: Task, printing: Boolean = true): Pair<Boolean?, Boolean?> {
    return testAssertions({task}, printing)
}

fun testAssertions(taskGenerator: () -> Task, printing:Boolean = true): Pair<Boolean?, Boolean?> {
    val task = taskGenerator()
    val id = task.id

    val notFound = "NOT FOUND"

    val partOne = task.part1(fromFile(id))

    val task2 = taskGenerator()
    val partTwo = task2.part2(fromFile(id))

    val expectedOne = answerPartOne(id)
    val expectedTwo = answerPartTwo(id)

    val isCorrect = { ans: Long?, exp: Long? ->
        if (ans == null || exp == null)
//                "UNKNOWN"
            null
        else
            (ans == exp)
    }

    var response = "Day number $id: \n"
    response += "Part one: answer = ${partOne ?: notFound}, expected = ${expectedOne ?: notFound}, correct = ${
        isCorrect(
            partOne,
            expectedOne
        ) ?: "UNKNOWN"
    }\n"
    response += "Part two: answer = ${partTwo ?: notFound}, expected = ${expectedTwo ?: notFound}, correct = ${
        isCorrect(
            partTwo,
            expectedTwo
        ) ?: "UNKNOWN"
    }\n"
    response += "\n"

    if (printing) {
        println(response)
    }

//    return isCorrect(partOne, expectedOne) == true && isCorrect(partTwo, expectedTwo) == true
    return isCorrect(partOne, expectedOne) to isCorrect(partTwo, expectedTwo)
}