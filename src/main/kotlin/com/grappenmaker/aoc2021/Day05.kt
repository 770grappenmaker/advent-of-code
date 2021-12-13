package com.grappenmaker.aoc2021

fun Solution.solveDay5(): Pair<Int, Int> {
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

    // Part one and two
    return getAnswer(lines.asSequence().filter { it.isStraight() }) to getAnswer(lines.asSequence())
}