package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day03() {
    fun solve(input: ECInput, func: (Point) -> Iterable<Point>): Int {
        val g = input.inputLines.asGrid { it == '#' }
        var prev = g.findPointsValued(true).toSet()
        var ans = prev.size

        while (prev.isNotEmpty()) {
            prev = prev.filterToSet { func(it).all { p -> p in prev } }
            ans += prev.size
        }

        return ans
    }

    partOne = solve(partOneInput, Point::adjacentSidesInf)
    partTwo = solve(partTwoInput, Point::adjacentSidesInf)
    partThree = solve(partThreeInput, Point::allAdjacentInf)
}