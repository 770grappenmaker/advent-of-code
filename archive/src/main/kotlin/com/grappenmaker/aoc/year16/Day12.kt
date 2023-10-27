package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day12() = puzzle(day = 12) {
    val insns = inputLines.parseProgram()
    fun solve(partTwo: Boolean) = VM(insns).apply {
        if (partTwo) registers['c'] = 1
        stepUntilHalted()
    }.registers.getValue('a').s()

    partOne = solve(false)
    partTwo = solve(true)
}