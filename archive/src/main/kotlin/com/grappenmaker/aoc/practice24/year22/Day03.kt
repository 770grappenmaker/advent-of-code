@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year22

import com.grappenmaker.aoc.*

fun PuzzleSet.day03() = puzzle(day = 3) {
    val elves = inputLines.map { l -> l.map { if (it in 'a'..'z') it - 'a' + 1 else it - 'A' + 27 } }

    partOne = elves.sumOf {
        val (l, r) = it.chunked(it.size / 2)
        l.toSet().intersect(r.toSet()).single()
    }

    partTwo = elves.map { it.toSet() }.chunked(3) { (a, b, c) -> (a intersect b intersect c).single() }.sum()
}