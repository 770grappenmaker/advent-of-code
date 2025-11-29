package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day04() = puzzle(day = 4) {
    var p1 = 0
    var p2 = 0

    outer@ for (p in inputLines) {
        val w = p.split(' ')
        if (w.hasDuplicate()) continue

        p1++

        for (a in w) for (b in w) {
            if (a == b) continue
            if (a.groupingBy { it }.eachCount() == b.groupingBy { it }.eachCount()) continue@outer
        }

        p2++
    }

    partOne = p1
    partTwo = p2
}