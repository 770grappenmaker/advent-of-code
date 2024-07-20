package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.permPairs
import com.grappenmaker.aoc.product

@PuzzleEntry
fun PuzzleSet.day1() = puzzle {
    val numbers = inputLines.map(String::toInt)
    val reverse = numbers.map { 2020 - it }
    val complement = numbers.first { it in reverse }
    partOne = (complement * (2020 - complement)).s()

    val perms = numbers.permPairs().map { it.toList() }
    val other = perms.first { 2020 - it.sum() in numbers }
    partTwo = ((2020 - other.sum()) * other.product()).s()
}