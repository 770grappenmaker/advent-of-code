package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day5() = puzzle(day = 5) {
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

    // Part one
    partOne = getAnswer(lines.asSequence().filter { it.isStraight() }).s()

    // Part two
    partTwo = getAnswer(lines.asSequence()).s()
}