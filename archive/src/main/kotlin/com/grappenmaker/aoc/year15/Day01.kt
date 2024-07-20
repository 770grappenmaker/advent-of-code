package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day1() = puzzle {
    partOne = (input.count { it == '(' } - input.count { it == ')' }).s()

//    partTwo = generateSequence(0 to 0) { (curr, idx) -> curr + (if (input[idx] == '(') 1 else -1) to idx + 1 }
//        .first { (a) -> a < 0 }.second.s()
    var current = 0
    partTwo = (input.indexOfFirst {
        current += if (it == '(') 1 else -1
        current < 0
    } + 1).s()
}