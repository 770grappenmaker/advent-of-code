package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*

fun PuzzleSet.day11() = puzzle(11) {
    fun sim(partTwo: Boolean) = with(startComputer(input)) {
        val enabled = if (partTwo) hashSetOf(Point(0, 0)) else hashSetOf()
        val visited = hashSetOf<Point>()

        var currentLocation = Point(0, 0)
        var currentDirection = Direction.UP

        input { if (currentLocation in enabled) 1 else 0 }

        while (!isHalted()) {
            visited += currentLocation

            if (stepUntilOutput() == 1L) enabled += currentLocation else enabled -= currentLocation
            if (isHalted()) break

            currentDirection = currentDirection.next(if (stepUntilOutput() == 1L) 1 else -1)
            currentLocation += currentDirection
        }

        enabled to visited
    }

    partOne = sim(false).second.size.s()
    partTwo = sim(true).first.asBooleanGrid().debug(on = "oo ", off = "   ")
}