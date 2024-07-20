package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle(day = 18) {
    val insns = inputLines.parseProgram()
    partOne = VM(insns).also { it.stepUntilHalt() }.recovered.s()

    val p0 = VM(insns, true, 0L)
    val p1 = VM(insns, true, 1L)

    p0.otherComputer = p1
    p1.otherComputer = p0

    while (p0.continues || p1.continues) {
        p0.step()
        p1.step()
    }

    partTwo = p1.sends.s()
}