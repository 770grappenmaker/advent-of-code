package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.rotate

fun PuzzleSet.day1() = puzzle {
    val digits = input.map(Char::digitToInt)
    fun solve(rot: Int) = digits.zip(digits.rotate(rot)).filter { (a, b) -> a == b }.sumOf { (a) -> a }.s()
    partOne = solve(1)
    partTwo = solve(digits.size / 2)
}