package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day1() = puzzle {
    val elves = input.split("\n\n").map { l -> l.lines().sumOf { it.toInt() } }.sortedDescending()
    partOne = elves.first().toString()
    partTwo = elves.take(3).sum().toString()
}