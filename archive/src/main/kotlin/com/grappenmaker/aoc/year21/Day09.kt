package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.points
import com.grappenmaker.aoc.asDigitGrid
import com.grappenmaker.aoc.floodFill
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.product

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(day = 9) {
    with(inputLines.asDigitGrid()) {
        val lowPoints = points.filter { p -> p.adjacentSides().all { this[p] < this[it] } }
        partOne = lowPoints.sumOf { this[it] + 1 }.toString()

        val basins = lowPoints.map { point -> floodFill(point, { this[it] != 9 }).size }.sortedDescending()
        partTwo = basins.take(3).product().toString()
    }
}
