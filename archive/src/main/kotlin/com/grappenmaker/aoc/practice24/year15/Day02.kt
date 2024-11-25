package com.grappenmaker.aoc.practice24.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day02() = puzzle(day = 2) {
    var p1 = 0L
    var p2 = 0L

    for (line in inputLines) {
        val (l, w, h) = line.split("x").map { it.toLong() }

        p1 += 2*l*w + 2*w*h + 2*h*l
        p1 += minOf(
            l * w,
            w * h,
            h * l
        )

        p2 += minOf(
            2 * l + 2 * w,
            2 * w + 2 * h,
            2 * h + 2 * l,
        )

        p2 += l * w * h
    }

    partOne = p1
    partTwo = p2
}