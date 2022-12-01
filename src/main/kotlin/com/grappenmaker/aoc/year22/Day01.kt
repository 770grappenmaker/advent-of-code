package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle {
    val elves = input
        .split("\n\n")
        .map { it.lines().map(String::toInt).sum() }
        .sortedDescending()

    partOne = elves.first().s()
    partTwo = (elves[0] + elves[1] + elves[2]).s()
}