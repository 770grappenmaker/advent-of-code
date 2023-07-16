package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*

fun PuzzleSet.day6() = puzzle(day = 6) {
    val points = inputLines.map { it.splitInts().asPair().toPoint() }
    val bound = points.boundary()
    val result = points.associateWith { 0 }.toMutableMap()
    val cachedPoints = bound.points

    for (p in cachedPoints) {
        val dists = points.map { (it manhattanDistanceTo p) to it }
        val (td, tp) = dists.minBy { (a) -> a }
        if (dists.any { (d, b) -> b != tp && d == td }) continue
        if (bound.onEdge(p)) result -= tp
        if (tp in result) result[tp] = result.getValue(tp) + 1
    }

    partOne = result.values.max().s()
    partTwo = cachedPoints.count { p -> points.sumOf { it manhattanDistanceTo p } < 10000 }.s()
}