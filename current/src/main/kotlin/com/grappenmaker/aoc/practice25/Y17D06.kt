package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day06() = puzzle(day = 6) {
    val banks = input.ints().toMutableList()
    val seen = hashSetOf<List<Int>>()
    var i = 0

    fun loop(cond: () -> Boolean) {
        do {
            var idx = banks.withIndex().maxBy { it.value }.index
            var v = banks[idx]
            banks[idx] = 0
            while (v > 0) {
                banks[(++idx) % banks.size]++
                v--
            }

            i++
        } while (cond())
    }

    loop { seen.add(banks.toList()) }
    partOne = i
    i = 0

    val goal = banks.toList()
    loop { goal != banks }
    partTwo = i
}