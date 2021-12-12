package com.grappenmaker.aoc2021

import kotlin.math.abs
import kotlin.math.min

fun Solution.solveDay7(): Pair<Int, Int> {
    // Part one
    val numbers = input.trim().split(",").map { it.toInt() }.sorted()
    val mid = numbers.size / 2
    val median = numbers[mid] + if (numbers.size % 2 != 0) numbers[mid - 1] else 0

    val partOne = numbers.sumOf { abs(it - median) }

    // Part two
    val mean = numbers.sum() / numbers.size
    val fuel = { i: Int -> i * (i + 1) / 2 }
    val calcFuel = { meanToUse: Int -> numbers.sumOf { fuel(abs(it - meanToUse)) } }
    val partTwo = min(calcFuel(mean), calcFuel(mean - 1))

    return partOne to partTwo
}