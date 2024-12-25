package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day22() = puzzle(day = 22) {
    data class Node(
        val x: Int,
        val y: Int,
        val max: Int,
        val used: Int,
        val available: Int
    )

    val nodes = inputLines.drop(2).map { l ->
        val (x, y, s, u, a) = l.splitInts()
        Node(x, y, s, u, a)
    }

    partOne = nodes.permPairsExclusive().count { (a, b) -> a.used != 0 && a.used <= b.available }.toString()

    val grid = nodes.associateBy { Point(it.x, it.y) }.asGrid()
    val goal = grid.topLeftCorner

    data class State(
        val curr: Point = grid.topRightCorner,
        val emptySpot: Point = grid.points.single { grid[it].used == 0 }
    )

    partTwo = bfsDistance(
        initial = State(),
        isEnd = { it.curr == goal },
        neighbors = { ( curr, emptySpot) ->
            with (grid) { emptySpot.adjacentSides() }
                .filter { grid[it].used != 0 && grid[it].used <= grid[emptySpot].max }
                .map { State(if (it == curr) emptySpot else curr, it) }
        }
    ).dist.toString()
}