package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    val grid = inputLines.asDigitGrid()
    partOne = grid.solve(grid.width, grid.height)
    partTwo = grid.asPseudoGrid(5).solve(grid.width, grid.height)
}

fun IntGrid.solve(width: Int, height: Int) = dijkstra(
    initial = topLeftCorner,
    end = bottomRightCorner,
    findCost = { p -> (this[p] + p.x / width + p.y / height).let { r -> r.takeIf { it < 10 } ?: (r - 9) } },
    diagonals = false
)!!.cost.toString()