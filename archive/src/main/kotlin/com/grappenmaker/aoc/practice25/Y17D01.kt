package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day01() = puzzle(day = 1) {
    val digits = input.map { it.digitToInt() }
    fun solve(rotate: Int) =
        digits.zip(digits.rotate(rotate)).sumOf { (a, b) -> if (a == b) a else 0 }

    partOne = solve(1)
    partTwo = solve(digits.size / 2)
}