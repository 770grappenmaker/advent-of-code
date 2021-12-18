package com.grappenmaker.aoc2021

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// Note: this line can only have a 0, 45 or 90 degree angle!
data class Line(val a: Point, val b: Point) {
    val pointsOnLine by lazy {
        if (isHorizontal()) {
            val minY = min(a.y, b.y)
            val maxY = max(a.y, b.y)
            (minY..maxY).map { Point(a.x, it) }
        } else if (isVertical()) {
            val minX = min(a.x, b.x)
            val maxX = max(a.x, b.x)
            (minX..maxX).map { Point(it, a.y) }
        } else {
            // Diagonal
            val xDir = if (b.x - a.x > 0) 1 else -1
            val yDir = if (b.y - a.y > 0) 1 else -1
            (0..abs(b.x - a.x)).map { Point(a.x + (xDir * it), a.y + (yDir * it)) }
        }
    }

    private fun isHorizontal() = a.x == b.x
    private fun isVertical() = a.y == b.y
    fun isStraight() = isHorizontal() || isVertical()
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.y, y - other.y)
}

operator fun Point.rangeTo(other: Point) = Line(this, other)