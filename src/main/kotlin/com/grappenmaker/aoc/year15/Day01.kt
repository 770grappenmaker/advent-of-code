package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle {
    partOne = (input.count { it == '(' } - input.count { it == ')' }).s()

    var current = 0
    partTwo = (input.indexOfFirst {
        current += if (it == '(') 1 else -1
        current < 0
    } + 1).s()
}