package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day16() = puzzle(day = 16) {
    fun List<Boolean>.dragon() = this + false + asReversed().map { !it }
    fun List<Boolean>.checksum(): List<Boolean> =
        if (size % 2 != 0) this else chunked(2) { (a, b) -> a == b }.checksum()

    fun solve(size: Int) = generateSequence(input.map { it == '1' }) { it.dragon() }
        .first { it.size >= size }.take(size).checksum().joinToString("") { if (it) "1" else "0" }

    partOne = solve(272)
    partTwo = solve(35651584)
}