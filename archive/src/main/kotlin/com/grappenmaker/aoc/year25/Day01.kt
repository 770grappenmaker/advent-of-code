package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.simplePuzzle
import kotlin.math.absoluteValue

fun main() = simplePuzzle(1, 2025) {
    var p1 = 0
    var p2 = 0
    var cur = 50

    for (l in inputLines) {
        val r = l.drop(1).toInt()
        val new = if (l.first() == 'L') cur - r else cur + r
        if (new <= 0 && cur != 0) p2++
        cur = new.mod(100)

        if (cur == 0) p1++
        p2 += new.absoluteValue / 100
    }

    partOne = p1
    partTwo = p2
}