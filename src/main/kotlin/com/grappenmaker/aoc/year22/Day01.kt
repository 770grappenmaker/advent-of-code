package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle {
    val elves = input.split("\n\n").map { l -> l.lines().sumOf { it.toInt() } }.sortedDescending()
    partOne = elves.first().s()
    partTwo = elves.take(3).sum().s()
}