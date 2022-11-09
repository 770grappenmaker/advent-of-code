package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle(day = 1) {
    // Part one
    val input = inputLines.map { it.toInt() }
    val countIncrements = { arr: List<Int> -> arr.asSequence().drop(1).filterIndexed { i, n -> n > arr[i] }.count() }
    partOne = countIncrements(input).s()

    // Part two
    partTwo = countIncrements(input.windowed(3).map { it.sum() }).s()
}