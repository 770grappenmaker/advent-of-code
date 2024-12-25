package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day24() = puzzle(day = 24) {
    val grid = inputLines.asCharGrid()
    val start = grid.findPointsValued('0').single()
    val target = grid.count { it in '0'..'9' }

    data class State(val curr: Point = start, val collected: Set<Point> = setOf(start))

    fun solve(partTwo: Boolean) = bfsDistance(
        State(),
        isEnd = { it.collected.size == target && (!partTwo || it.curr == start) },
        neighbors = { (curr, collected) ->
            with(grid) { curr.adjacentSides() }.filterNot { grid[it] == '#' }.map { next ->
                State(next, if (grid[next] in '0'..'9' && next !in collected) collected + next else collected)
            }
        }
    ).dist.toString()

    partOne = solve(false)
    partTwo = solve(true)
}