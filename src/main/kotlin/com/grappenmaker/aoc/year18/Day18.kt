package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*

fun PuzzleSet.day18() = puzzle(day = 18) {
    val initial = inputLines.asCharGrid()
    fun solve(n: Int) = initial.patternRepeating(n) { curr ->
        with(curr) {
            mapIndexedElements { point, curr ->
                val adj = point.allAdjacentElements()
                when (curr) {
                    '.' -> if (adj.countContains('|') >= 3) '|' else curr
                    '|' -> if (adj.countContains('#') >= 3) '#' else curr
                    '#' -> if ('#' in adj && '|' in adj) curr else '.'
                    else -> error("Impossible")
                }
            }
        }
    }

    fun Grid<Char>.value() = countContains('|') * countContains('#')
    partOne = solve(10).value().s()
    partTwo = solve(1000000000).value().s()
}