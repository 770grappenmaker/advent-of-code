package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day5() = puzzle {
    val nums = inputLines.map(String::toInt)
    fun solve(partTwo: Boolean): String {
        val mut = nums.toMutableList()
        var idx = 0
        var steps = 0

        while (idx in nums.indices) {
            idx += if (partTwo && mut[idx] >= 3) mut[idx]-- else mut[idx]++
            steps++
        }

        return steps.s()
    }

    partOne = solve(false)
    partTwo = solve(true)
}