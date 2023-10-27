package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*

fun PuzzleSet.day11() = puzzle(day = 11) {
    val serial = input.toInt()

    val cache = mutableMapOf<Point, Long>()
    fun Point.value() = cache.getOrPut(this) {
        val rack = x + 10
        val start = (rack * y + serial) * rack
        (start / 100 % 10 - 5).toLong()
    }

    fun Point.format() = "$x,$y"

    val sizeCache = mutableMapOf<Pair<Point, Int>, Long>()

    fun recurse(start: Point, side: Int): Long = sizeCache.getOrPut(start to side) {
        if (side == 1) start.value() else {
            var sum = 0L
            val endX = start.x + side - 1
            val endY = start.y + side - 1

            for (y in start.y..endY) {
                sum += Point(endX, y).value()
            }

            for (x in start.x..<endX) {
                sum += Point(x, endY).value()
            }

            recurse(start, side - 1) + sum
        }
    }

    fun forSize(side: Int) = Rectangle(Point(1, 1), Point(301 - side, 301 - side)).points.maxBy { recurse(it, side) }

    partOne = forSize(3).format()

    // SLOW D:
    // I've heard there is a faster method, but this is what I came up with
    // It's some clever method to quickly sum subsections of grids, using just 4 lookups in a simple table
    val (p, i) = (1..300).map { forSize(it) to it }.maxBy { (a, s) -> recurse(a, s) }
    partTwo = "${p.format()},$i"
}