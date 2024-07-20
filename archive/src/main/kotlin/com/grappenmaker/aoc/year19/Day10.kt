package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.atan2

@PuzzleEntry
fun PuzzleSet.day10() = puzzle(day = 10) {
    with(inputLines.asGrid { it == '#' }) {
        val asteroids = filterTrue().toSet()
        val (loc, count) = asteroids.map { start ->
            start to (asteroids - start).map { start.angle(it) }.toSet().count()
        }.maxBy { it.second }

        partOne = count.s()
        val (x, y) = (asteroids - loc).groupBy { loc.angle(it) }.toList()
            .flatMap { (angle, v) -> v.sortedBy { loc manhattanDistanceTo it }.map { angle to it }.withIndex() }
            .sortedBy { it.value.first }.sortedBy { it.index }[199].value.second

        partTwo = (x * 100 + y).s()
    }
}

private fun Double.rangedAngle() = if (this < 0.0) this + 360.0 else this
private fun Point.angle(other: Point) =
    (Math.toDegrees(atan2((other.y - y).toDouble(), (other.x - x).toDouble())) + 90.0).rangedAngle()