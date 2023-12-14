package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day13() = puzzle(day = 13) {
    val gs = input.doubleLines().map { it.lines().asGrid { c -> c == '#' } }

    fun solveDir(match: Int, view: BooleanGrid.() -> List<List<Boolean>>) = gs.sumOfNotNull { g ->
        val slices = g.view()
        (1..<slices.size).firstNotNullOfOrNull { row ->
            val (l, r) = slices.splitAt(row)
            val off = l.asReversed().zip(r) { a, b -> a.zip(b).count { (c, d) -> c != d } }.sum()
            if (off == match) l.size else null
        }
    }

    fun solve(match: Int) = (100 * solveDir(match) { rowsValues } + solveDir(match) { columnsValues }).s()
    partOne = solve(0)
    partTwo = solve(1)
}