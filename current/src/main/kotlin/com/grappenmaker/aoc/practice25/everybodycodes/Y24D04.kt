package com.grappenmaker.aoc.practice25.everybodycodes

import kotlin.math.abs

fun ECSolveContext.day04() {
    fun ECInput.solve(index: (Int) -> Int): Int {
        val ints = inputLines.map { it.toInt() }.sorted()
        val m = ints[index(ints.size)]
        return ints.sumOf { abs(it - m) }
    }

    partOne = partOneInput.solve { 0 }
    partTwo = partTwoInput.solve { 0 }
    partThree = partThreeInput.solve { it / 2 }
}