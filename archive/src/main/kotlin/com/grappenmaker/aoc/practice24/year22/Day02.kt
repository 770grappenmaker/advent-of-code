@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year22

import com.grappenmaker.aoc.*

fun PuzzleSet.day02() = puzzle(day = 2) {
    var p1 = 0
    var p2 = 0

    for (l in inputLines) {
        val (f, _, s) = l
        val o = f - 'A'
        val res = s - 'X'

        p1 += res + 1
        p1 += when (o) {
            ((res + 1) % 3) -> 0
            res -> 3
            else -> 6
        }

        p2 += (o + (res - 1)).mod(3) + 1
        p2 += res * 3
    }

    partOne = p1
    partTwo = p2
}