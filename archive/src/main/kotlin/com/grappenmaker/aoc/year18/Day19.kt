package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(day = 19) {
    val computer = inputLines.parseVM()

    fun run(partTwo: Boolean): Int = with(computer) {
        if (partTwo) registers[0] = 1

        stepUntilHalt { if (partTwo && cycles == 100) halt() }
        (if (partTwo) registers.max() else registers.first()).also { reset() }
    }

    partOne = run(false).toString()

    val toFactorize = run(true)
    partTwo = (1..toFactorize).filter { toFactorize % it == 0 }.sum().toString()
}