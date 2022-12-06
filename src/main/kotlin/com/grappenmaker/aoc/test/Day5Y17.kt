package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle

fun main() = simplePuzzle(5, 2017) {
    val nums = inputLines.map(String::toInt)
    fun solve(partTwo: Boolean): String {
        val mut = nums.toMutableList()
        var idx = 0
        var steps = 0

        while (idx in nums.indices) {
            idx += if (mut[idx] >= 3 && partTwo) mut[idx]-- else mut[idx]++
            steps++
        }

        return steps.s()
    }

    partOne = solve(false)
    partTwo = solve(true)
}