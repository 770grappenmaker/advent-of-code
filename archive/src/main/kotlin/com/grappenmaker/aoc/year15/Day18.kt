package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle {
    // mutable state better?
    fun solve(partTwo: Boolean) = inputLines.asGrid { it == '#' }.automaton { loc, on ->
        val onNeighbours = loc.allAdjacentIndexed().count { (p, no) -> no || (partTwo && p in corners) }
        when {
            partTwo && loc in corners -> true
            on && onNeighbours in 2..3 -> true
            !on && onNeighbours == 3 -> true
            else -> false
        }
    }.nth(100).countTrue().s()

    partOne = solve(false)
    partTwo = solve(true)
}