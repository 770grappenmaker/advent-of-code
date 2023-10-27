package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle(day = 1) {
    val input = inputLines.map { it.toInt() }
    partOne = input.zipWithNext().count { (a, b) -> b > a }.s()
    partTwo = input.windowed(4).count { (a, _, _, d) -> d > a }.s()
}