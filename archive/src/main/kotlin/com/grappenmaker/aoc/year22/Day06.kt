package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allDistinct
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day6() = puzzle {
    fun solve(amount: Int) = (input.windowed(amount).indexOfFirst { it.asIterable().allDistinct() } + amount).toString()
    partOne = solve(4)
    partTwo = solve(14)
}