package com.grappenmaker.aoc2021

import kotlin.math.abs

fun Solution.solveDay17(): Answer {
    // Get instruction
    val instruction = inputLines.first().substring(13).split(", ")
        .map { range ->
            val fromTo = range.substring(2).split("..").map { it.toInt() }
            fromTo.minOrNull()!! to fromTo.maxOrNull()!!
        }

    // Get target area
    val (x1, x2) = instruction.first()
    val (y1, y2) = instruction.last()
    val target = Rectangle(Point(x1, y1), Point(x2, y2))

    // Bruteforce all possible positions
    val paths = (0..target.b.x).flatMap { x ->
        (target.a.y..abs(target.a.y)).mapNotNull { y ->
            val path = Point(x, y).getPath(target)
            if (path.any { it in target }) path else null
        }
    }

    return paths.maxOf { path -> path.maxOf { it.y } } to paths.size
}

// Point is a velocity here
// rect is the target
private fun Point.getPath(rect: Rectangle, initalPos: Point = Point(0, 0)): MutableList<Point> {
    var pos = initalPos
    var velocity = this
    val positions = mutableListOf<Point>()

    // While we have not overshot, find the path
    while (!(pos.x > rect.b.x || pos.y < rect.a.y)) {
        pos += velocity
        velocity += Point(if (velocity.x > 0) -1 else if (velocity.x < 0) 1 else 0, -1)
        positions.add(pos)
    }

    return positions
}

private data class Rectangle(val a: Point, val b: Point) {
    operator fun contains(point: Point) = point.x in (a.x..b.x) && point.y in (a.y..b.y)
}