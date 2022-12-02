package tasks.day2

import base.Task

class RockPaperScissors : Task {
    override val id: Int
        get() = 2

    override fun part1(lines: Sequence<String>): Long {
        return splitLines(lines)
            .map { tokens ->
                val rival = rivalMove(tokens[0])
                val me = myMove(tokens[1])

                singleRound(rival, me)
            }
            .sum()
    }

    override fun part2(lines: Sequence<String>): Long {
        return splitLines(lines)
            .map { tokens ->
                val rival = rivalMove(tokens[0])
                val outcome = when (tokens[1]) {
                    "X" -> Outcome.LOSE
                    "Y" -> Outcome.DRAW
                    "Z" -> Outcome.WIN
                    else -> throw Exception("Unexpected token! X, Y or Z expected but found: '${tokens[1]}'")
                }

                roundPartTwo(rival, outcome)
            }
            .sum()
    }

    private fun singleRound(rival: Move, me: Move): Long {
        val myChoice = mapOf(Move.ROCK to 1, Move.PAPER to 2, Move.SCISSORS to 3)
        val prize = when (rival) {
            Move.ROCK -> mapOf(Move.ROCK to 3, Move.PAPER to 6, Move.SCISSORS to 0)
            Move.PAPER -> mapOf(Move.ROCK to 0, Move.PAPER to 3, Move.SCISSORS to 6)
            Move.SCISSORS -> mapOf(Move.ROCK to 6, Move.PAPER to 0, Move.SCISSORS to 3)
        }

        return (myChoice[me]!! + prize[me]!!).toLong()
    }

    private fun roundPartTwo(rival: Move, outcome: Outcome): Long {
        return when (outcome) {
            Outcome.WIN -> when (rival) {
                Move.ROCK -> singleRound(rival, Move.PAPER)
                Move.PAPER -> singleRound(rival, Move.SCISSORS)
                Move.SCISSORS -> singleRound(rival, Move.ROCK)
            }

            Outcome.DRAW -> singleRound(rival, rival)

            Outcome.LOSE -> when (rival) {
                Move.ROCK -> singleRound(rival, Move.SCISSORS)
                Move.PAPER -> singleRound(rival, Move.ROCK)
                Move.SCISSORS -> singleRound(rival, Move.PAPER)
            }
        }
    }

    private fun splitLines(lines: Sequence<String>): Sequence<List<String>> {
        val regex = "[ABC] [XYZ]".toRegex()

        return lines
            .filter { it.matches(regex) }
            .map { it.split("\\s+".toRegex()) }
    }


    private fun rivalMove(move: String) = when (move) {
        "A" -> Move.ROCK
        "B" -> Move.PAPER
        "C" -> Move.SCISSORS
        else -> throw Exception("Unexpected token! A, B or C expected but found: '$move'")
    }

    private fun myMove(move: String) = when (move) {
        "X" -> Move.ROCK
        "Y" -> Move.PAPER
        "Z" -> Move.SCISSORS
        else -> throw Exception("Unexpected token! X, Y or Z expected but found: '$move'")
    }

    private enum class Move {
        ROCK,
        PAPER,
        SCISSORS
    }

    private enum class Outcome {
        WIN,
        DRAW,
        LOSE
    }

}
