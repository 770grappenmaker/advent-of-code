package com.grappenmaker.aoc.year22

import kotlin.math.max
import kotlin.math.min

val dAdjacentSides = listOf(
    1 to 0,
    0 to 1,
    -1 to 0,
    0 to -1
).map { it.toPoint() }

val dAdjacentDiagonals = listOf(
    1 to 1,
    1 to -1,
    -1 to 1,
    -1 to -1
).map { it.toPoint() }

val dAllAdjacent = dAdjacentSides + dAdjacentDiagonals

data class Point(val x: Int, val y: Int)

fun Pair<Int, Int>.toPoint() = Point(first, second)
fun Point.toIndex(width: Int) = x * width + y
fun pointFromIndex(index: Int, width: Int) = Point(index % width, index / width)

private fun Point.getAdjacent(of: List<Point>, width: Int, height: Int) =
    of.map { this + it }.filter { it.x in 0 until width && it.y in 0 until height }

fun Point.adjacentSides(width: Int, height: Int) = getAdjacent(dAdjacentSides, width, height)
fun Point.adjacentDiagonals(width: Int, height: Int) = getAdjacent(dAdjacentDiagonals, width, height)
fun Point.allAdjacent(width: Int, height: Int) = getAdjacent(dAllAdjacent, width, height)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
operator fun Point.times(tim: Int) = Point(x * tim, y * tim)
operator fun Point.div(by: Int) = Point(x / by, y / by)
operator fun Point.rem(with: Int) = Point(x % with, y % with)
operator fun Point.rangeTo(other: Point) = Line(this, other)

data class Line(val a: Point, val b: Point) {
    val minX get() = min(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxX get() = max(a.x, b.x)
    val maxY get() = max(a.y, b.y)
}

fun Pair<Point, Point>.toLine() = Line(first, second)

operator fun Line.contains(some: Point) = some.x in minX..maxX && some.y in minY..maxY
