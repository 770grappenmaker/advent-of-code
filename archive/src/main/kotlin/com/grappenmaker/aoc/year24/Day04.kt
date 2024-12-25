@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day04() = puzzle(day = 4) {
    // code is slow, we ball
    val g = inputLines.asCharGrid()

    fun solve(
        test: List<Char>,
        points: (Point) -> Iterable<Iterable<Pair<Point, (Point) -> Point>>>,
    ): Int {
        val rev = test.asReversed()
        return g.points.sumOf { s ->
            points(s).count { run ->
                run.all { (p, d) ->
                    val v = generateSequence(p, d).takeWhile { it in g }.map { g[it] }.take(test.size).toList()
                    v == test || v == rev
                }
            }
        }
    }

    partOne = solve("XMAS".toList()) { p ->
        listOf(
            listOf(p to { it + UP }),
            listOf(p to { it + LEFT }),
            listOf(p to { it + UP + LEFT }),
            listOf(p to { it + UP + RIGHT }),
        )
    }

    partTwo = solve("MAS".toList()) { p ->
        if (g[p] != 'A') return@solve emptyList()
        listOf(listOf((p + UP + LEFT) to { it + DOWN + RIGHT }, (p + DOWN + LEFT) to { it + UP + RIGHT }))
    }
}