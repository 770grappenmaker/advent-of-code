package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.abs
import kotlin.math.min

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(day = 7) {
    // Part one
    val numbers = input.trim().split(",").map { it.toInt() }.sorted()
    val mid = numbers.size / 2
    val median = numbers[mid] + if (numbers.size % 2 != 0) numbers[mid - 1] else 0

    partOne = numbers.sumOf { abs(it - median) }.s()

    // Part two
    val mean = numbers.sum() / numbers.size
    val fuel = { i: Int -> i * (i + 1) / 2 }
    val calcFuel = { meanToUse: Int -> numbers.sumOf { fuel(abs(it - meanToUse)) } }
    partTwo = min(calcFuel(mean), calcFuel(mean - 1)).s()
}