@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day13() = puzzle(day = 13) {
    var p1 = 0L
    var p2 = 0L

    for (ls in input.doubleLines()) {
        val (ax, ay, bx, by, pxo, pyo) = ls.lines().flatMap { l ->
            l.split(", ").map { s -> s.takeLastWhile { it.isDigit() }.toInt() }
        }

        val det = ax * by - ay * bx
        if (det == 0) continue

        fun solve(px: Long, py: Long): Long {
            val id = px * by - py * bx
            val jd = py * ax - px * ay
            if (id % det != 0L || jd % det != 0L) return 0L

            val i = id / det
            val j = jd / det
            return 3 * i + j
        }

        p1 += solve(pxo.toLong(), pyo.toLong())
        p2 += solve(pxo + 10000000000000, pyo + 10000000000000)
    }

    partOne = p1
    partTwo = p2
}