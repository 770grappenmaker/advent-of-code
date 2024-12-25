package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.simplify
import com.grappenmaker.aoc.width

@PuzzleEntry
fun PuzzleSet.day20() = puzzle(day = 20) {
    val ranges = inputLines.map { l ->
        val (a, b) = l.split("-").map(String::toLong)
        a..b
    }.simplify()

//    partOne = (0L..Long.MAX_VALUE).first { v -> ranges.none { v in it } }.s()
    partOne = ranges.map { it.last + 1 }.sorted().first { v -> ranges.none { v in it } }.toString()
    partTwo = ranges.fold(0x100000000L) { acc, curr -> acc - curr.width() }.toString()
}