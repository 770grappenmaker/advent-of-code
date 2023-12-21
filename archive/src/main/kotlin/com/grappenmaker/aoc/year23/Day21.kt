package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day21() = puzzle(day = 21) {
    val g = inputLines.asCharGrid()
    val s = g.findPointsValued('S').single()
    val gs = g.mapElements { it != '#' }
    fun Point.avail() = gs[Point(x.mod(gs.width), y.mod(gs.height))]

    fun solve(a: Int) = setOf(s).applyN(a) { s -> s.flatMapToSet { c -> c.adjacentSidesInf().filter { it.avail() } } }
    partOne = solve(64).size.s()

    val (a, b, c) = List(3) { solve(65 + it * 131).size.toLong() }
    val x = 202300L
    partTwo = (a + (b - a) * x + (x * (x - 1) / 2) * (c - 2 * b + a)).s()
}