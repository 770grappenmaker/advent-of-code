@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day08() = puzzle(day = 8) {
    val g = inputLines.asCharGrid().asMutableGrid()
    fun solve(find: (Point, Point) -> Sequence<Point>): Int {
        val ans = hashSetOf<Point>()

        for ((k, vs) in g.points.groupBy { g[it] }) {
            if (k == '.') continue
            for ((a, b) in vs.permPairsExclusive()) for (point in find(a, b)) if (point in g) ans += point
        }

        return ans.size
    }

    partOne = solve { a, b -> sequenceOf(a - b + a, b - a + b) }
    partTwo = solve { a, b ->
        val diff = a - b
        (generateSequence(a) { it + diff } + generateSequence(b) { it - diff }).takeWhile { it in g }
    }
}