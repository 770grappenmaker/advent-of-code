package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction.*

fun PuzzleSet.day14() = puzzle {
    val lines = inputLines.flatMap { l ->
        l.split(" -> ").map { it.split(",").map(String::toInt).asPair().toPoint() }
            .windowed(2).map { (a, b) -> a..b }.connect()
    }

    val sandLocation = Point(500, 0)
    val directions = listOf(DOWN.toPoint(), DOWN + LEFT, DOWN + RIGHT)

    fun HashSet<Point>.sim(partTwo: Boolean): Int {
        val p2h = if (partTwo) lines.maxY() + 2 else lines.maxY()
        val yRange = 0..p2h
        var sand = 0

        fun isFilled(p: Point) = if (partTwo && p.y == p2h) true else p in this

        outer@ while (true) {
            var curr = sandLocation
            while (true) {
                val poss = directions.map { curr + it }.filter { it.y in yRange }
                if (poss.isEmpty()) break@outer

                val new = poss.firstOrNull { !isFilled(it) }
                if (new != null) {
                    curr = new
                    continue
                }

                if (isFilled(curr)) break@outer
                this += curr
                sand++
                break
            }
        }

        return sand
    }

    partOne = lines.toHashSet().sim(false).s()
    partTwo = lines.toHashSet().sim(true).s()
}