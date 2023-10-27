package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day20() = puzzle(day = 20) {
    val grid = inputLines.asCharGrid()
    val valid = grid.findPointsValued('.').toSet()

    data class Portal(val name: String, val at: Point)

    val portals = with(grid) {
        valid.mapNotNull { p ->
            listOf(
                p + LEFT + LEFT to p + LEFT,
                p + DOWN to p + DOWN + DOWN,
                p + UP + UP to p + UP,
                p + RIGHT to p + RIGHT + RIGHT
            ).singleOrNull { (a, b) -> a in grid && b in grid && this[a] in 'A'..'Z' && this[b] in 'A'..'Z' }
                ?.let { (a, b) -> Portal(this[a].toString() + this[b].toString(), p) }
        }
    }

    val byName = portals.groupBy { it.name }
    val links = buildMap {
        byName.forEach { (_, v) ->
            if (v.size == 2) {
                val (a, b) = v
                put(a.at, b.at)
                put(b.at, a.at)
            }
        }
    }

    val start = byName.getValue("AA").single().at
    val end = byName.getValue("ZZ").single().at

    val insideX = valid.minX() + 1..<valid.maxX()
    val insideY = valid.minY() + 1..<valid.maxY()

    data class State(val at: Point, val layer: Int = 0)

    fun solve(partTwo: Boolean) = bfsDistance(
        initial = State(start),
        isEnd = { it.at == end && it.layer == 0 },
        neighbors = { (at, layer) ->
            at.adjacentSidesInf().filter { it in valid }.map { State(it, layer) } + listOfNotNull(links[at]).map { p ->
                val newLayer = when {
                    !partTwo -> 0
                    at.x in insideX && at.y in insideY -> layer + 1
                    else -> layer - 1
                }

                State(p, newLayer)
            }.filterNot { it.layer < 0 }
        }
    ).dist.s()

    partOne = solve(false)
    partTwo = solve(true)
}