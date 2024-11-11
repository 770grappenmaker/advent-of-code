package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day05() = puzzle(day = 5) {
    val lines = inputLines.map { l ->
        val (f, t) = l.split(" -> ").map { p ->
            val (a, b) = p.split(",").map { it.toInt() }
            Point(a, b)
        }

        f..t
        Line(f, t)
    }

    fun List<Line>.solve(): Int {
        val seenOnce = hashSetOf<Point>()
        val seenTwice = hashSetOf<Point>()
        var ans = 0

        for (line in this) for (pt in line.allPointsSequence()) if (!seenOnce.add(pt) && seenTwice.add(pt)) ans++

        return ans
    }

    partOne = lines.filter { !it.isDiagonal }.solve()
    partTwo = lines.solve()
}