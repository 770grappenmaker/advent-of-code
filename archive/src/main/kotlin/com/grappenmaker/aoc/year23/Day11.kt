package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day11() = puzzle(day = 11) {
    val g = inputLines.asGrid { it == '#' }
    val galax = g.findPointsValued(true)
    val emptyRows = g.rowsValues.withIndex().filter { it.value.all { p -> !p } }.map { it.index }.toSet()
    val emptyCols = g.columnsValues.withIndex().filter { it.value.all { p -> !p } }.map { it.index }.toSet()

    fun solve(n: Long) = galax.map { p ->
        PointL(
            emptyCols.count { it < p.x } * n + p.x,
            emptyRows.count { it < p.y } * n + p.y
        )
    }.combinations(2).sumOf { (a, b) -> a manhattanDistanceTo b }.s()

    partOne = solve(1)
    partTwo = solve(1000000 - 1)
}