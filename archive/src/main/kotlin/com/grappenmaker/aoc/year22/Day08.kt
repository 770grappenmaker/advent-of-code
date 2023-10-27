package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*

fun PuzzleSet.day8() = puzzle {
    with (inputLines.asDigitGrid()) {
        partOne = points.count { p ->
            val (lc, rc) = column(p.x).splitAtExcluding(p.y)
            val (lr, rr) = row(p.y).splitAtExcluding(p.x)
            val check = { points: List<Point> -> points.all { this[it] < this[p] } }
            check(lc) || check(rc) || check(lr) || check(rr)
        }.s()

        partTwo = points.maxOf { p ->
            val (lc, rc) = column(p.x).splitAtExcluding(p.y)
            val (lr, rr) = row(p.y).splitAtExcluding(p.x)
            fun find(points: List<Point>) = (points.findIndexOf { this[it] >= this[p] } ?: points.indices.last) + 1
            find(lc.asReversed()) * find(rc) * find(lr.asReversed()) * find(rr)
        }.s()
    }
}