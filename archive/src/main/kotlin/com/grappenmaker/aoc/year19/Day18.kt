package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle(day = 18) {
    fun Char.isKey() = this in 'a'..'z'

    val grid = inputLines.asCharGrid()
    val totalKeys = grid.elements.count(Char::isKey)

    fun Grid<Char>.adj(point: Point, keys: Set<Char>) = point.adjacentSides().filter {
        when (val v = this[it]) {
            in 'A'..'Z' -> v.lowercaseChar() in keys
            '#' -> false
            else -> true
        }
    }

    data class SolveDistance(val key: Point, val moved: Point, val distance: Int)
    data class MemoEntry(val keys: Set<Char>, val pos: Set<Point>)

    fun Grid<Char>.solve(
        keys: Set<Char> = emptySet(),
        pos: Set<Point> = findPointsValued('@').toSet(),
        memo: MutableMap<MemoEntry, Int> = mutableMapOf()
    ): Int = memo.getOrPut(MemoEntry(keys, pos)) {
        if (keys.size == totalKeys) return@getOrPut 0

        pos.flatMap { toMove ->
            buildList {
                // Modded version of fillDistance
                val queue = queueOf(toMove to 0)
                val seen = hashSetOf(toMove)

                queue.drain { (curr, dist) ->
                    val value = this@solve[curr]
                    if (value.isKey() && value !in keys) add(SolveDistance(curr, toMove, dist))
                    else adj(curr, keys).forEach { if (seen.add(it)) queue.addFirst(it to dist + 1) }
                }
            }
        }.minOf { (key, moved, distance) -> solve(keys + this[key], pos - moved + key, memo) + distance }
    }

    partOne = grid.solve().toString()
    partTwo = grid.asMutableGrid().apply {
        val center = Point(width / 2, height / 2)
        val toAdd = """
            @#@
            ###
            @#@
        """.trimIndent().lines().asCharGrid()

        val start = center - Point(toAdd.width / 2, toAdd.height / 2)
        toAdd.points.forEach { p -> this[start + p] = toAdd[p] }
    }.asGrid().solve().toString()
}