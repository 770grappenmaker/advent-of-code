package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day1() = puzzle(day = 1) {
    val d = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val s = d.mapTo(hashSetOf()) { it.first() }

    fun solve(partTwo: Boolean) = inputLines.sumOf { l ->
        // optimized method
        l.indices.mapNotNull { i ->
            l[i].digitToIntOrNull() ?: if (partTwo) d.takeIf { l[i] in s }
                ?.findIndexOf { l.regionMatches(i, it, 0, it.length) }?.let(Int::inc) else null
        }.let { 10 * it.first() + it.last() }

        // clean method
        // l.windowed(5, partialWindows = true).mapNotNull { w ->
        //     w.first().digitToIntOrNull() ?: if (partTwo) d.findIndexOf { w.startsWith(it) }?.let(Int::inc) else null
        // }.let { 10 * it.first() + it.last() }
    }.toString()

    partOne = solve(false)
    partTwo = solve(true)
}