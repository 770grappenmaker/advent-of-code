package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day5() = puzzle(day = 5) {
    val lines = inputLines.map { line ->
        val points = line.split(" -> ").map { pair ->
            val nums = pair.split(",")
            Point(nums[0].toInt(), nums[1].toInt())
        }

        points[0]..points[1]
    }

    // Util for both parts
     fun Sequence<Line>.getAnswer() = flatMap { it.allPoints() }
         .fold(mutableMapOf<com.grappenmaker.aoc.Point, Int>()) { acc, point ->
             if (!acc.containsKey(point)) acc[point] = 0
             acc[point] = acc.getValue(point) + 1
             acc
         }.count { it.value >= 2 }

    // Part one
    partOne = lines.asSequence().filter { it.isStraight() }.getAnswer().s()

    // Part two
    partTwo = lines.asSequence().getAnswer().s()
}