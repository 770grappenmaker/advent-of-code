package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day05() = puzzle(day = 5) {
    val n = input.ints()

    fun solve(p1: Boolean): Int {
        val nums = n.toMutableList()
        var pos = 0
        var steps = 0

        while (pos >= 0 && pos < nums.size) {
            val orig = nums[pos]
            nums[pos] += if (orig >= 3 && !p1) -1 else 1
            pos += orig
            steps++
        }

        return steps
    }

    partOne = solve(true)
    partTwo = solve(false)
}