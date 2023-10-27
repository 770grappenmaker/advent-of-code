package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.queueOf
import com.grappenmaker.aoc.rotateInPlace

fun PuzzleSet.day17() = puzzle(day = 17) {
    val steps = input.toInt()
    val curr = queueOf(0)

    fun update(range: IntRange) = range.forEach {
        curr.rotateInPlace(-steps)
        curr.addLast(it)
    }

    update(1..2017)
    partOne = curr.first().s()

    // This works, but let's not
    // update(2018..50000000)
    // partTwo = curr[curr.indexOf(0) + 1].s()

    // Optimized!
    var idx = 0
    var ans: Int? = null
    (1..50000000).forEach {
        idx = (idx + steps) % it
        if (idx++ == 0) ans = it
    }

    partTwo = ans.s()
}