package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day14() = puzzle(day = 14) {
    val start = inputLines.asCharGrid()
    val maze = start.mapElements { if (it == 'O') '.' else it }

    // optimized solution
    val ps = start.findPointsValued('O').toSet()

    fun Set<Point>.isEnd(p: Point, d: Direction) = p + d !in maze || maze[p + d] != '.' || p + d in this
    fun Set<Point>.step(d: Direction): Set<Point> {
        val res = toHashSet()

        for (p in this) {
            if (isEnd(p, d)) continue

            res -= p
            res += p + d
        }

        return res
    }

    fun Set<Point>.solve() = sumOf { start.height - it.y }.s()
    fun Set<Point>.roll(d: Direction) = generateSequence(this) { it.step(d) }.first { g -> g.all { g.isEnd(it, d) } }

    partOne = ps.roll(UP).solve()
    partTwo = ps.patternRepeating(1000000000) { it.roll(UP).roll(LEFT).roll(DOWN).roll(RIGHT) }.solve()

    // sane solution
//    fun Grid<Char>.step(d: Direction): Grid<Char> = asMutableGrid().apply {
//        findPointsValued('O').asSequence().filter { it + d in this && this[it + d] == '.' }.forEach { p ->
//            this[p + d] = 'O'
//            this[p] = '.'
//        }
//    }.asGrid()
//
//    fun Grid<Char>.solve() = findPointsValued('O').sumOf { height - it.y }.s()
//    fun Grid<Char>.roll(d: Direction) = generateSequence(this) { it.step(d) }.first { g ->
//        g.findPointsValued('O').all { it + d !in g || g[it + d] != '.' }
//    }
//
//    partOne = start.roll(UP).solve()
//    partTwo = start.patternRepeating(1000000000) { it.roll(UP).roll(LEFT).roll(DOWN).roll(RIGHT) }.solve()
}