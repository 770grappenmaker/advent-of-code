package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day17() = puzzle(day = 17) {
    data class State(
        val pos: Point = Point(0, 0),
        val sameDir: Int = 0,
        val lastDir: Direction? = null,
    )

    val g = inputLines.asDigitGrid()
    val t = g.bottomRightCorner
    fun solve(
        n: Int,
        cond: (State) -> Boolean = { true },
    ) = dijkstra(initial = State(), isEnd = { it.pos == t && cond(it) }, neighbors = { s ->
        when {
            s.lastDir == null -> Direction.entries
            s.sameDir == n -> listOf(s.lastDir.next(1), s.lastDir.next(-1))
            cond(s) -> Direction.entries - setOf(-s.lastDir)
            else -> listOf(s.lastDir)
        }.filter { s.pos + it in g }.map {
            State(
                pos = s.pos + it,
                sameDir = if (it == s.lastDir) s.sameDir + 1 else 1,
                lastDir = it
            )
        }
    }, findCost = { g[it.pos] })!!.cost.s()

    // advent of reading comprehension "(or even before it can stop at the end)"
    partOne = solve(3)
    partTwo = solve(10) { it.sameDir >= 4 }
}