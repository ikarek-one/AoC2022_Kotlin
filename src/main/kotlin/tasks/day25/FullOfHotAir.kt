package tasks.day25

import base.Task
import kotlin.math.pow


class FullOfHotAir : Task {
    override val id: Int
        get() = 25

    override fun part1(lines: Sequence<String>): Long {
        val sum = lines.filter { it.isNotBlank() }
            .map { snafuToDecimal(it) }
            .sum()

        val snafu = decimalToSnafu(sum)
        println("Day 25, part 1, SNAFU = '$snafu'")

        // only decimal form is checked here, the SNAFU answer was: '2=-0=1-0012-=-2=0=01'
        return sum
    }

    private fun snafuToDecimal(snafuAsText: String): Long {
        return snafuAsText
            .reversed()
            .withIndex()
            .map { (exp, ch) ->
                val factor = digitToSnafu[ch] ?: throw Exception("Unexpected char: '$ch'!")
                baseAsDouble.pow(exp).toLong() * factor
            }.sum()
    }

    private fun decimalToSnafu(dec: Long): String {
        if (dec == 0L) return ""
        val lastSnafuDig = snafuDigits[(dec % base).toInt()]

        return decimalToSnafu((dec + shift) / base) + lastSnafuDig
    }

    private companion object {
        val digitToSnafu = mapOf<Char, Long>(
            '2' to 2,
            '1' to 1,
            '0' to 0,
            '-' to -1,
            '=' to -2
        )

        val snafuDigits = listOf('0', '1', '2', '=', '-')

        val base = 5L
        val baseAsDouble = base.toDouble()
        val shift = 2
    }

}