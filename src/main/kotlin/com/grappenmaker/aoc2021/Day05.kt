package com.grappenmaker.aoc2021

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Solution.solveDay5() {
    // Part one
    val lines = inputLines.map { line ->
        val points = line.split(" -> ").map { pair ->
            val nums = pair.split(",")
            Point(nums[0].toInt(), nums[1].toInt())
        }

        points[0]..points[1]
    }

    // Util for both parts
    val getAnswer = { seq: Sequence<Line> ->
        seq.flatMap { it.pointsOnLine }
            .fold(mutableMapOf<Point, Int>()) { acc, point ->
                if (!acc.containsKey(point)) acc[point] = 0
                acc[point] = acc[point]!! + 1
                acc
            }.count { it.value >= 2 }
    }

    val partOne = getAnswer(lines.asSequence().filter { it.isStraight() })
    println("Part one: $partOne")

    // Part two
    println("Part two: ${getAnswer(lines.asSequence())}")
}

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

data class Point(val x: Int, val y: Int)

operator fun Point.rangeTo(other: Point) = Line(this, other)