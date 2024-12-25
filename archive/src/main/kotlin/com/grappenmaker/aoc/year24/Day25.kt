@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    val locks = mutableListOf<List<Int>>()
    val keys = mutableListOf<List<Int>>()

    for (l in input.doubleLines()) {
        val g = l.lines().asCharGrid()
        val counts = g.xRange.map { g.columnValues(it).countContains('#') }
        (if (g.rowValues(0).all { it == '#' }) locks else keys) += counts
    }

    var ans = 0
    for (lock in locks) for (key in keys) if (lock.zip(key).all { (f, s) -> f + s <= 7 }) ans++
    partOne = ans
}