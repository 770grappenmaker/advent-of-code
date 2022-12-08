package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

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
            val find = { points: List<Point>, edge: Point ->
                (points.find { this[it] >= this[p] } ?: edge) manhattanDistanceTo p
            }

            find(lc.asReversed(), Point(p.x, 0)) * find(rc, Point(p.x, height - 1)) *
                    find(lr.asReversed(), Point(0, p.y)) * find(rr, Point(width - 1, p.y))
        }.s()
    }
}