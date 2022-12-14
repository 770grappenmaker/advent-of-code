package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day12() = puzzle {
    val charGrid = inputLines.asCharGrid()
    val start = charGrid.points.first { charGrid[it] == 'S' }
    val end = charGrid.points.first { charGrid[it] == 'E' }
    val grid = inputLines.asGrid {
        when (it) {
            'S' -> 0
            'E' -> 25
            else -> it - 'a'
        }
    }

    with(grid) {
        fun eval(start: Point, cond: (Point) -> Boolean) =
            bfsDistance(start, cond) { it.adjacentSides().filter { p -> this[it] - this[p] <= 1 } }

        partOne = eval(end) { it == start }.s()
        partTwo = (eval(end) { this[it] == 0 }).s()
    }
}