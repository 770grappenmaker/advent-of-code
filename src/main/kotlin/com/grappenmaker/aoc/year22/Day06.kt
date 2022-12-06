package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day6() = puzzle {
    fun solve(amount: Int) = (input.windowed(amount).indexOfFirst { it.asIterable().allDistinct() } + amount).s()
    partOne = solve(4)
    partTwo = solve(14)
}