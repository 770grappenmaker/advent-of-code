package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.deepen
import com.grappenmaker.aoc.year22.swapOrder

fun PuzzleSet.day6() = puzzle {
    val inps = inputLines.deepen().swapOrder()
    fun solve(mul: Int) =
        inps.joinToString("") { c -> c.groupingBy { it }.eachCount().maxBy { it.value * mul }.key.toString() }

    partOne = solve(1)
    partTwo = solve(-1)
}