package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year22.deepen
import com.grappenmaker.aoc.year22.rotate

fun main() = simplePuzzle(1, 2017) {
    val digits = input.deepen().map(Char::digitToInt)
    fun solve(rot: Int) = digits.zip(digits.rotate(rot)).filter { (a, b) -> a == b }.sumOf { (a) -> a }.s()
    partOne = solve(1)
    partTwo = solve(digits.size / 2)
}