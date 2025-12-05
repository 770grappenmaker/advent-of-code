package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(5, 2025) {
    val (ranges, nums) = input.split("\n\n")
    val rs = ranges.lines().map { it.split("-").map(String::toLong).let { (a, b) -> a..b } }

    partOne = nums.lines().count { n -> rs.any { n.toLong() in it } }
    partTwo = rs.simplify().sumOf { it.last - it.first + 1 }
}