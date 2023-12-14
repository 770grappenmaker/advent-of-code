package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day14() = puzzle(day = 14) {
    val start: CharGrid = inputLines.asCharGrid()
    val order = listOf(
        start.columns.flatten(),
        start.rows.flatMap { it.asReversed() },
        start.columns.flatMap { it.asReversed() },
        start.rows.flatten(),
    )

    fun CharGrid.roll(d: Direction): CharGrid = asMutableGrid().apply {
        for (p in order[d.ordinal]) {
            if (this[p] != 'O') continue

            var curr = p
            while (curr + d in this && this[curr + d] == '.') {
                this[curr] = '.'
                curr += d
                this[curr] = 'O'
            }
        }
    }

    fun GridLike<Char>.solve() = findPointsValued('O').sumOf { height - it.y }.s()
    partOne = start.roll(UP).solve()
    partTwo = start.patternRepeating(1000000000) { it.roll(UP).roll(LEFT).roll(DOWN).roll(RIGHT) }.solve()
}