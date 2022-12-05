package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day3() = puzzle {
    val rucksacks = inputLines.map { l -> l.map { if (it in 'A'..'Z') it - 'A' + 27 else it - 'a' + 1 } }
    partOne = rucksacks.sumOf { sack -> sack.splitHalf().let { (l, r) -> l.first { it in r } } }.s()
    partTwo = rucksacks.chunked(3).sumOf { (a, b, c) -> a.first { e -> e in b && e in c } }.s()
}