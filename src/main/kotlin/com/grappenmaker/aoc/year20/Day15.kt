package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.splitInts

fun PuzzleSet.day15() = puzzle(day = 15) {
    val starting = input.splitInts()
    val lookup = mutableMapOf<Int, Int>()
    var last = 0

    fun walk(range: IntRange): String {
        for (turn in range) {
            val toSay = when {
                turn in starting.indices -> starting[turn]
                last in lookup -> turn - lookup.getValue(last) - 1
                else -> 0
            }

            lookup[last] = turn - 1
            last = toSay
        }

        return last.s()
    }

    partOne = walk(0 until 2020)
    partTwo = walk(2020 until 30000000)
}