package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle {
    val chars = input.trim()
    partOne = (chars.count { it == '(' } - chars.count { it == ')' }).s()

    var current = 0
    partTwo = (chars.withIndex().find { (_, c) -> 
        current += if (c == '(') 1 else -1
        current < 0
    }!!.index + 1).s()
}