package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day2() = puzzle {
    fun List<Pair<Int, Int>>.score() = sumOf { (l, r) ->
        when {
            l == r -> 3
            (r - l).mod(3) == 1 -> 6
            else -> 0
        } + r + 1
    }

    val inputs = inputLines.map { it[0] - 'A' to it[2] - 'X' }
    partOne = inputs.score().toString()
    partTwo = inputs.map { (l, r) ->
        l to (l + when (r) {
            0 -> -1
            2 -> 1
            else -> 0
        }).mod(3)
    }.score().toString()
}