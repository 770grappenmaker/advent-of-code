package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day10() = puzzle(10) {
    val adapters = inputLines.map(String::toInt)
    val deviceAdapter = adapters.max() + 3
    val toConnect = (listOf(0) + adapters + deviceAdapter).sorted()
    val dRange = 1..3

    val result = toConnect.windowed(2).groupingBy { (a, b) -> b - a }.eachCount()
    partOne = (result.getValue(1) * result.getValue(3)).toString()

    partTwo = buildMap {
        this[0] = 1L
        toConnect.drop(1).forEach { v -> this[v] = dRange.sumOf { d -> this[v - d] ?: 0L } }
    }[deviceAdapter].toString()
}