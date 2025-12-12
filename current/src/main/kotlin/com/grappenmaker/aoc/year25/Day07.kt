package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day07() = puzzle(day = 7) {
    var p1 = 0
    val g = inputLines.asCharGrid()
    val s = g.findPointsValued('S').single()
    val memo = hashMapOf<Point, Long>()

    fun recur(head: Point): Long = memo.getOrPut(head) {
        val d = head + Direction.DOWN
        if (d !in g) return 1

        if (g[d] == '^') {
            p1++
            recur(d + Direction.LEFT) + recur(d + Direction.RIGHT)
        } else recur(d)
    }

    partTwo = recur(s)
    partOne = p1
}