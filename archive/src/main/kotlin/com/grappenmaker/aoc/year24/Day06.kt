@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.UP

fun PuzzleSet.day06() = puzzle(day = 6) {
    val g = inputLines.asCharGrid()
    val start = g.findPointsValued('^').single()

    data class State(val pos: Point, val dir: Direction)

    fun State.update(grid: CharGrid): State =
        if (grid[pos + dir] == '#') copy(dir = dir.next(1)) else copy(pos = pos + dir)

    fun CharGrid.seq() = generateSequence(State(start, UP)) { it.update(this) }
        .takeUntilGraceful { it.pos + it.dir in this }

    fun CharGrid.isLoop(): Boolean {
        val dup = hashSetOf<State>()
        for (state in seq().take(10000)) if (!dup.add(state)) return true
        return false
    }

    val p1pos = hashSetOf<Point>()
    g.seq().forEach { p1pos += it.pos }
    partOne = p1pos.size

    partTwo = p1pos.asSequence().filter { g[it] == '.' }.count {
        val copy = g.asMutableGrid()
        copy[it] = '#'
        copy.isLoop()
    }
}