package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(19) {
    fun Point.eval() = input.evalProgram(x.toLong(), y.toLong()) == 1L
    partOne = Rectangle(Point(0, 0), Point(49, 49)).points.count { it.eval() }.s()

    var x = 0
    var y = 0
    while (!Point(x + 99, y).eval()) {
        y++
        while (!Point(x, y + 99).eval()) x++
    }

    partTwo = (x * 10000 + y).s()
}