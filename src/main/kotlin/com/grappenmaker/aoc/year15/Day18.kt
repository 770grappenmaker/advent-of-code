package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.*

fun PuzzleSet.day18() = puzzle {
    // mutable state better?
    fun solve(partTwo: Boolean) = generateSequence(inputLines.asGrid { it == '#' }) {
        with (it) {
            mapIndexedElements { loc, on ->
                val onNeighbours = loc.allAdjacentIndexed()
                    .count { (p, no) -> no || (partTwo && p in corners) }

                when {
                    partTwo && loc in corners -> true
                    on && onNeighbours in 2..3 -> true
                    !on && onNeighbours == 3 -> true
                    else -> false
                }
            }
        }
    }.drop(1).take(100).last().countTrue().s()

    partOne = solve(false)
    partTwo = solve(true)
}