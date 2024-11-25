@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year20

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day03() = puzzle(day = 3) {
    val g = inputLines.asGrid { it == '#' }

    fun solve(slope: Point): Int {
        var curr = Point(0, 0)

        var ans = 0
        while (curr.y in 0..<g.height) {
            if (g[curr]) ans++

            curr += slope
            curr = curr.copy(x = curr.x % g.width)
        }

        return ans
    }

    partOne = solve(Point(3, 1))

    var res = 1L
    res *= solve(Point(3, 1))
    res *= solve(Point(1, 1))
    res *= solve(Point(5, 1))
    res *= solve(Point(7, 1))
    partTwo = res * solve(Point(1, 2))
}