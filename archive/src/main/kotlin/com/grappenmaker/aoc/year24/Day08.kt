@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day08() = puzzle(day = 8) {
    val g = inputLines.asCharGrid()
    fun solve(find: (Point, Point) -> Sequence<Point>): Int {
        val ans = hashSetOf<Point>()

        for ((k, vs) in g.points.groupBy { g[it] }) {
            if (k == '.') continue
            for ((a, b) in vs.permPairsExclusive()) for (point in find(a, b)) if (point in g) ans += point
        }

        return ans.size
    }

    partOne = solve { a, b -> sequenceOf(a - b + a, b - a + b) }
    partTwo = solve { a, b -> (a - b).let { diff -> (generateSequence(a) { it + diff }).takeWhile { it in g } } }
}