package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*

fun PuzzleSet.day17() = puzzle(day = 17) {
    val initial = inputLines.asGrid { it == '#' }.filterTrue().map { (x, y) -> PointND(listOf(x, y, 0, 0)) }

    fun Set<PointND>.step(): Set<PointND> {
        val result = hashSetOf<PointND>()
        ((minBound() - 1)..(maxBound() + 1)).points.forEach { p ->
            val activeNeighs = p.adjacent().count { it in this }
            val shouldActivate = when (p) {
                in this -> activeNeighs == 2 || activeNeighs == 3
                else -> activeNeighs == 3
            }

            if (shouldActivate) result += p
        }

        return result
    }

    fun solve(dims: Int) = generateSequence(initial.map { PointND(it.coords.take(dims)) }.toSet()) { it.step() }
        .drop(1).take(6).last().size.s()

    partOne = solve(3)
    partTwo = solve(4)
}