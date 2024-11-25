package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day04() = puzzle(day = 4) {
    val dl = input.split("\n\n")
    val od = dl.first().split(",").map(String::toInt)
    val bs = dl.drop(1).map { s ->
        val g = s.lines().map { it.splitInts() }.asGrid().asMutableGrid()
        g to mutableBooleanGrid(g.width, g.height)
    }

    val wins = mutableListOf<Long>()

    for (n in od) for ((nums, marked) in bs) {
        if (marked.isEmpty()) continue

        val mark = nums.findPointsValued(n).singleOrNull() ?: continue
        marked[mark] = true

        if (marked.rowValues(mark.y).all() || marked.columnValues(mark.x).all()) {
            val s = nums.pointsSequence.filter { !marked[it] }.sumOf { nums[it] }
            wins += s.toLong() * n.toLong()
            nums.clear()
            marked.clear()
        }
    }

    partOne = wins.first()
    partTwo = wins.last()
}