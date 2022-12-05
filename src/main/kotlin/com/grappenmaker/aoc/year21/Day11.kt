package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.*
import com.grappenmaker.aoc.year22.Point

fun PuzzleSet.day11() = puzzle(day = 11) {
    with(inputLines.asMutableDigitGrid()) {
        var flashes = 0
        var steps = 0

        fun update(point: Point) {
            if (this[point] != -1) this[point]++
            if (this[point] < 10) return

            flashes++
            this[point] = -1

            point.allAdjacent().forEach { update(it) }
        }

        val step = {
            steps++
            points.forEach(::update)
            mapInPlace { if (it == -1) 0 else it }
        }

        repeat(100) { step() }
        partOne = flashes.s()

        while (any { it != 0 }) step()
        partTwo = steps.s()
    }
}