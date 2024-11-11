package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*

fun PuzzleSet.day02() = puzzle(day = 2) {
    fun solve(grid: String): String {
        val g = grid.trimIndent().lines().map { it.split(" ") }.asGrid()
        var curr = g.findPointsValued("5").single()
        var res = ""

        for (l in inputLines) {
            for (c in l) {
                val prev = curr

                when (c) {
                    'U' -> curr += Direction.UP
                    'D' -> curr += Direction.DOWN
                    'L' -> curr += Direction.LEFT
                    'R' -> curr += Direction.RIGHT
                }

                if (curr !in g || g[curr] == "0") curr = prev
            }

            res += g[curr]
        }

        return res
    }

    partOne = solve("""
        1 2 3
        4 5 6
        7 8 9
    """.trimIndent())

    partTwo = solve("""
        0 0 1 0 0
        0 2 3 4 0
        5 6 7 8 9
        0 A B C 0
        0 0 D 0 0
    """.trimIndent())
}