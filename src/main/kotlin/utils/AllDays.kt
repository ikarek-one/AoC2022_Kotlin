package utils

import base.Task
import tasks.day01.Calories
import tasks.day02.RockPaperScissors
import tasks.day03.Rucksacks
import tasks.day04.Overlapping
import tasks.day05.StackedCrates
import tasks.day06.MessageMarker
import tasks.day07.NoSpaceLeft
import tasks.day08.TreetopHouse

val allTasks = listOf<Task>(
    // day 1
    Calories(),

    // day 2
    RockPaperScissors(),

    // day 3
    Rucksacks(),

    // day 4
    Overlapping(),

    // day 5
    StackedCrates(),

    // day 6
    MessageMarker(),

    // day 7
    NoSpaceLeft(),

    // day 8
    TreetopHouse(),

    // day 9

)

fun testAll() {
    allTasks.forEach(::testAssertions)
}

fun main() {
    testAll()
}