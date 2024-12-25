package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day5() = puzzle(day = 5) {
    val lines = inputLines.map { l ->
        l.split(" -> ").map { it.split(",").map(String::toInt).asPair().toPoint() }.let { (a, b) -> a..b }
    }

    fun Iterable<Line>.solve() = flatMap { it.allPoints() }.frequencies().count { it.value >= 2 }
    partOne = lines.filter { it.isStraight }.solve().toString()
    partTwo = lines.solve().toString()
}