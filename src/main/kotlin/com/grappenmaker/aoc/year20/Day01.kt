package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.permutationPairs
import com.grappenmaker.aoc.year22.product

fun PuzzleSet.day1() = puzzle {
    val numbers = inputLines.map(String::toInt)
    val reverse = numbers.map { 2020 - it }
    val complement = numbers.first { it in reverse }
    partOne = (complement * (2020 - complement)).s()

    val perms = numbers.permutationPairs().map { it.toList() }
    val other = perms.first { 2020 - it.sum() in numbers }
    partTwo = ((2020 - other.sum()) * other.product()).s()
}