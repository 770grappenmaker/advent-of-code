package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day02() {
    fun <T> solveEarly(reverse: Boolean, input: ECInput, choose: (IntRange) -> Iterable<T>): Int {
        val ls = input.inputLines
        val runic = ls.first().substringAfter(':').split(',')
        var ans = 0

        for (l in ls.drop(1)) {
            val seen = hashSetOf<T>()

            for (w in runic) for (i in 0..l.length - w.length) {
                val s = l.substring(i, i + w.length)
                if (s == w || (reverse && s.reversed() == w)) seen += choose(i..<i + w.length)
            }

            ans += seen.size
        }

        return ans
    }

    partOne = solveEarly(false, partOneInput) { listOf(it.first) }
    partTwo = solveEarly(true, partTwoInput) { it }

    val ls = partThreeInput.inputLines
    val runic = ls.first().substringAfter(':').split(',')

    val g = ls.drop(2).asCharGrid()
    val p3 = hashSetOf<Point>()

    // slow but cba
    for (w in runic) {
        // hor
        for (y in g.yRange) for (x in 0..<g.width) {
            val pts = (Point(x, y)..Point(x + w.length - 1, y)).allPointsSequence()
                .map { it.mapX { px -> px % g.width } }.toList()

            val s = pts.map { g[it] }.joinToString("")
            if (s == w || s.reversed() == w) p3 += pts
        }

        // vert
        for (x in g.xRange) for (y in 0..g.height - w.length) {
            val pts = (Point(x, y)..Point(x, y + w.length - 1)).allPoints()
            val s = pts.map { g[it] }.joinToString("")
            if (s == w || s.reversed() == w) p3 += pts
        }
    }

    partThree = p3.size
}