package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day9() = puzzle(day = 9) {
    partOne = input.evalProgram(1).s()
    partTwo = input.evalProgram(2).s()
}