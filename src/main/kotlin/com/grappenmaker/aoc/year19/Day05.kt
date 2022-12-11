package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day5() = puzzle {
    partOne = input.evalProgram(1).s()
    partTwo = input.evalProgram(5).s()
}