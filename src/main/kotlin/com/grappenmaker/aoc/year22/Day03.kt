package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day3() = puzzle {
    val rucksacks = inputLines.map { it.toPriorities() }
    partOne = rucksacks.sumOf { rucksack ->
        val (l, r) = rucksack.toCompartments()
        l.first { it in r }
    }.s()

    partTwo = rucksacks.chunked(3).sumOf { (a, b, c) -> a.first { e -> e in b && e in c } }.s()
}

fun String.toPriorities() = map { if (it.isUpperCase()) it - 'A' + 27 else it - 'a' + 1 }
fun List<Int>.toCompartments() = (size / 2).let { subList(0, it) to subList(it, size) }