package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day13() = puzzle(day = 13) {
    val gs = input.doubleLines().map { it.lines().asGrid { c -> c == '#' } }

    fun solveDir(
        partTwo: Boolean,
        view: BooleanGrid.() -> List<List<Boolean>>,
        range: BooleanGrid.() -> IntRange,
    ) = gs.sumOfNotNull { g ->
        val slices = g.view()
        g.range().firstNotNullOfOrNull { row ->
            val (l, r) = slices.splitAt(row)
            val ms = minOf(l.size, r.size)

            val ra = l.takeLast(ms)
            val rb = r.take(ms).asReversed()
            val off = ra.indices.sumOf { a -> ra[a].indices.count { ra[a][it] != rb[a][it] } }

            if (off == (if (partTwo) 1 else 0)) l.size else null
        }
    }

    fun solve(partTwo: Boolean) = (100 * solveDir(partTwo, BooleanGrid::rowsValues) { 1..<height } +
            solveDir(partTwo, BooleanGrid::columnsValues) { 1..<width }).s()

    partOne = solve(false)
    partTwo = solve(true)
}