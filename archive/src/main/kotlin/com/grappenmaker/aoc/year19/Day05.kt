package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day5() = puzzle {
    partOne = input.evalProgram(1).toString()
    partTwo = input.evalProgram(5).toString()
}