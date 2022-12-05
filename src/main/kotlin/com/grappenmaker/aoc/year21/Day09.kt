package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.points
import com.grappenmaker.aoc.year22.asDigitGrid
import com.grappenmaker.aoc.year22.floodFill
import com.grappenmaker.aoc.year22.product

fun PuzzleSet.day9() = puzzle(day = 9) {
    with(inputLines.asDigitGrid()) {
        val lowPoints = points.filter { p -> p.adjacentSides().all { this[p] < this[it] } }
        partOne = lowPoints.sumOf { this[it] + 1 }.s()

        val basins = lowPoints.map { point -> floodFill(point, { this[it] != 9 }).size }.sortedDescending()
        partTwo = basins.take(3).product().s()
    }
}
