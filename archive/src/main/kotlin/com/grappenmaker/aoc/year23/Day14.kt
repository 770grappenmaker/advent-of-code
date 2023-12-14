package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day14() = puzzle(day = 14) {
    val start = inputLines.asCharGrid()

    // TODO: think about solving it by rolling just the set of points
    fun Grid<Char>.step(d: Direction): Grid<Char> = asMutableGrid().apply {
        findPointsValued('O').asSequence().filter { it + d in this && this[it + d] == '.' }.forEach { p ->
            this[p + d] = 'O'
            this[p] = '.'
        }
    }.asGrid()

    fun Grid<Char>.solve() = findPointsValued('O').sumOf { height - it.y }.s()
    fun Grid<Char>.roll(d: Direction) = generateSequence(this) { it.step(d) }.first { g ->
        g.findPointsValued('O').all { it + d !in g || g[it + d] != '.' }
    }

    partOne = start.roll(UP).solve()
    partTwo = start.patternRepeating(1000000000) { it.roll(UP).roll(LEFT).roll(DOWN).roll(RIGHT) }.solve()
}