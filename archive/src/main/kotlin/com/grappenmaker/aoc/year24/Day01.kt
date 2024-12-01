@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.absoluteValue

fun PuzzleSet.day01() = puzzle(day = 1) {
    val al = mutableListOf<Int>()
    val bl = mutableListOf<Int>()

    for (l in inputLines) {
        val (a, b) = l.ints()
        al += a
        bl += b
    }

    al.sort()
    bl.sort()

    val freq = bl.frequencies()
    partOne = al.zip(bl).sumOf { (a, b) -> (a - b).absoluteValue }
    partTwo = al.sumOf { it * freq.getOrDefault(it, 0) }
}