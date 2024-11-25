package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.*
import kotlin.math.absoluteValue

fun PuzzleSet.day07() = puzzle(day = 7) {
    val poss = input.split(",").map { it.toInt() }
    val range = (poss.min())..(poss.max())

    fun compute(formula: (Int) -> Int) = range.minOf { p ->
        poss.sumOf { a -> formula((p - a).absoluteValue) }
    }

    partOne = compute { it }
    partTwo = compute { it * (it + 1) / 2 }
}