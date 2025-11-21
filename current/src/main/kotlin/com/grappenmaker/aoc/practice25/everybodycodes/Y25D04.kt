package com.grappenmaker.aoc.practice25.everybodycodes

import java.math.BigDecimal
import java.math.RoundingMode

fun ECSolveContext.y25day04() {
    fun ECInput.solve(start: BigDecimal, mode: RoundingMode, modify: List<String>.() -> List<String>): BigDecimal {
        var curr = start
        var prev = inputLines.modify().first().toInt()

        for (l in inputLines.modify().drop(1)) {
            val rat = l.toInt()
            curr *= prev.toBigDecimal()
            curr /= rat.toBigDecimal()
            prev = rat
        }

        return curr.setScale(0, mode)
    }

    partOne = partOneInput.solve(2025.toBigDecimal(), RoundingMode.FLOOR) { this }
    partTwo = partTwoInput.solve(10000000000000.toBigDecimal(), RoundingMode.CEILING) { asReversed() }

    var ans = 100L

    for (l in partThreeInput.inputLines) {
        val parts = l.split("|").map { it.toInt() }
        if (parts.size == 1) if (ans == 100L) ans *= parts[0] else ans /= parts[0]
        else ans *= parts[1] / parts[0]
    }

    partThree = ans
}