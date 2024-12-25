package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day6() = puzzle(day = 6) {
    // oh boy do we love optimizing problems needlessly
    fun solve(t: Long, d: Long): Long {
        var min = 0L
        var max = t / 2

        while (min + 1 < max) {
            val pivot = (min + max) / 2
            val found = pivot * (t - pivot)
            val s = found - d

            when {
                s < 0L -> min = pivot
                s > 0L -> max = pivot
            }
        }

        return t - 2 * min - 1
    }

    partOne = inputLines.map { l -> l.substringAfter(':').trim().split(" ").filter { it.isNotEmpty() }.map { it.toLong() } }
        .let { (t, d) -> t.zip(d, ::solve).product() }.toString()

    partTwo = inputLines.map { it.filter(Char::isDigit).toLong() }.let { (t, d) -> solve(t, d) }.toString()
}