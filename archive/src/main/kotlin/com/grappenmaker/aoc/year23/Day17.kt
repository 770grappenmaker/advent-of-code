package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day17() = puzzle(day = 17) {
    val g = inputLines.asDigitGrid()

    data class State(
        val pos: Point = Point(0, 0),
        val sameDir: Int = 0,
        val lastDir: Direction? = null,
    )

    val t = g.bottomRightCorner
    fun solve(
        cond: (State) -> Boolean = { true },
        dirs: (State) -> List<Direction>,
    ) = dijkstra(initial = State(), isEnd = { it.pos == t && cond(it) }, neighbors = { s ->
        dirs(s).filter { s.pos + it in g }.map {
            State(
                pos = s.pos + it,
                sameDir = if (it == s.lastDir) s.sameDir + 1 else 1,
                lastDir = it
            )
        }
    }, findCost = { g[it.pos] })!!.cost.s()

    partOne = solve {
        when {
            it.lastDir == null -> Direction.entries
            it.sameDir == 3 -> listOf(it.lastDir.next(1), it.lastDir.next(-1))
            else -> Direction.entries - setOf(-it.lastDir)
        }
    }

    // advent of reading comprehension "(or even before it can stop at the end)"
    partTwo = solve(cond = { it.sameDir >= 4 }) {
        when {
            it.lastDir == null -> Direction.entries
            it.sameDir == 10 -> listOf(it.lastDir.next(1), it.lastDir.next(-1))
            it.sameDir >= 4 -> Direction.entries - setOf(-it.lastDir)
            else -> listOf(it.lastDir)
        }
    }
}