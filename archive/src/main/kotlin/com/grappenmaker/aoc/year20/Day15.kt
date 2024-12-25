package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    val starting = input.splitInts()
    val lookup = IntArray(30000000) { -1 }
    var last = 0

    fun walk(range: IntRange): String {
        for (turn in range) {
            val toSay = when {
                turn in starting.indices -> starting[turn]
                lookup[last] != -1 -> turn - lookup[last] - 1
                else -> 0
            }

            lookup[last] = turn - 1
            last = toSay
        }

        return last.toString()
    }

    partOne = walk(0..<2020)
    partTwo = walk(2020..<30000000)
}