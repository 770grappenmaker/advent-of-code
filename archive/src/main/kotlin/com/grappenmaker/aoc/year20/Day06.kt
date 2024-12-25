package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day6() = puzzle {
    val groups = input.split("\n\n").map { g -> g.lines().map(String::deepen).map { it.toSet() } }
    partOne = groups.sumOf { g -> g.reduce { a, c -> a + c }.size }.toString()
    partTwo = groups.sumOf { g -> g.reduce { a, c -> a intersect c }.size }.toString()
}