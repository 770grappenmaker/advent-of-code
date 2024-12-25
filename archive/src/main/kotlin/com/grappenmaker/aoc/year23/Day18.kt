package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle(day = 18) {
    fun solve(partTwo: Boolean): String {
        var p = 0L
        return (inputLines.scan(PointL(0, 0)) { a, l ->
            val (d, c, cl) = l.split(" ")
            val ad = if (partTwo) "RDLU"[cl.dropLast(1).last().digitToInt(16)] else d.single()
            val dir = when (ad) {
                'R' -> RIGHT
                'U' -> UP
                'D' -> DOWN
                'L' -> LEFT
                else -> error("Impossible")
            }

            val len = if (partTwo) cl.substringAfter('#').dropLast(2).toLong(16) else c.toLong()
            p += len
            a + (dir * len)
        }.asReversed().zipWithNext().sumOf { (a, b) -> (a.x + b.x) * (a.y - b.y) } / 2 + p / 2 + 1).toString()
    }

    partOne = solve(false)
    partTwo = solve(true)
}