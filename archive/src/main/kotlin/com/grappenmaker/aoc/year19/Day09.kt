package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(day = 9) {
    partOne = input.evalProgram(1).toString()
    partTwo = input.evalProgram(2).toString()
}