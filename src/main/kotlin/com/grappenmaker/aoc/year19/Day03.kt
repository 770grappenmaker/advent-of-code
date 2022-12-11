package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.*

fun PuzzleSet.day3() = puzzle {
    val (a, b) = inputLines.map { l ->
        l.split(",").map { d -> d.first().parseDirection() * d.drop(1).toInt() }
            .scan(Point(0, 0)) { a, c -> a + c }.windowed(2) { (a, b) -> a..b }.connect()
    }

    val intersections = a.drop(1).intersect(b.drop(1).toSet())
    partOne = intersections.minOf { it.manhattanDistance }.s()
    partTwo = intersections.minOf { a.indexOf(it) + b.indexOf(it) }.s()
}