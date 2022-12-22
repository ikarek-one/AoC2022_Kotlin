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
import tasks.day09.RopeBridge
import tasks.day10.CathodeRayTube
import tasks.day13.DistressSignal
import tasks.day14.RegolithReservoir
import tasks.day18.BoilingBoulders
import tasks.day21.MonkeyMath

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
    RopeBridge(),

    // day 10
    CathodeRayTube(),

    // day 11
    FakeTask(11),

    // day 12
    FakeTask(12),

    //day 13
    DistressSignal(),

    // day 14
    RegolithReservoir(),

    // day 15

    // day 18
    BoilingBoulders(),

    // day 21
    MonkeyMath(),

)

class FakeTask(val idx: Int) : Task {
    override val id: Int
        get() = idx
}

fun testAll() {
    allTasks.forEach(::testAssertions)
}

fun main() {
    testAll()
}