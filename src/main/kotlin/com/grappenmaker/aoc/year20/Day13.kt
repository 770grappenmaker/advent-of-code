package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day13() = puzzle(day = 13) {
    val (startTimePart, busesPart) = inputLines
    val startTime = startTimePart.toLong()
    val buses = busesPart.split(",").map { if (it == "x") null else it.toLong() }

    val ignored = buses.filterNotNull()
    partOne = generateSequence(startTime) { it + 1 }.withIndex()
        .firstNotNullOf { (i, t) -> ignored.firstOrNull { t % it == 0L }?.let { i.toLong() * it } }.s()

    val byIndex = buses.mapIndexedNotNull { idx, v -> v?.let { idx to it } }
    var totalPeriod = 1L
    var totalTime = 0L

    byIndex.forEach { (offset, period) ->
        totalTime = generateSequence(totalTime) { it + totalPeriod }.first { t -> (t + offset) % period == 0L }
        totalPeriod *= period
    }

    partTwo = totalTime.s()
}