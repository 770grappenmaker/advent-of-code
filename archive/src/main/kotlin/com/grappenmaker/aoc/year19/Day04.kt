package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.parseRange
import com.grappenmaker.aoc.toDigits

@PuzzleEntry
fun PuzzleSet.day4() = puzzle {
    val range = input.parseRange()
    fun solve(partTwo: Boolean) = range.count { p ->
        val digits = p.toDigits()
        digits.windowed(2).filterIndexed { idx, (a, b) ->
            a == b && (!partTwo || digits.getOrNull(idx - 1) != a && digits.getOrNull(idx + 2) != b)
        }.any() && digits.sortedDescending() == digits
    }.s()

    partOne = solve(false)
    partTwo = solve(true)
}