package com.grappenmaker.aoc.practice24.year15

import com.grappenmaker.aoc.*

fun PuzzleSet.day03() = puzzle(day = 3) {
    fun solve(p2: Boolean): Int {
        var pos = Point(0, 0)
        var otherPos = Point(0, 0)
        val visited = hashSetOf(pos)

        var isRobot = false

        for (char in input) {
            val curr = if (isRobot) otherPos else pos
            val res = when (char) {
                '^' -> curr + Direction.UP
                'v' -> curr + Direction.DOWN
                '<' -> curr + Direction.LEFT
                '>' -> curr + Direction.RIGHT
                else -> error("Impossible")
            }

            if (isRobot) otherPos = res else pos = res
            visited += res
            if (p2) isRobot = !isRobot
        }

        return visited.size
    }

    partOne = solve(false)
    partTwo = solve(true)
}