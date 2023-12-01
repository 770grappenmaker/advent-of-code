package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day1() = puzzle(day = 1) {
    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    fun solve(partTwo: Boolean) = inputLines.map { l ->
        (if (partTwo) digits.foldIndexed(l) { idx, acc, curr ->
            acc.replace(curr, curr.first().s() + (idx + 1) + curr.last())
        } else l).filter { it.isDigit() }.map { it.digitToInt() }.let { it.first() to it.last() }
    }.sumOf { (a, b) -> 10 * a + b }.s()

    partOne = solve(false)
    partTwo = solve(true)
}