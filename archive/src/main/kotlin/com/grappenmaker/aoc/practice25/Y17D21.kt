package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(21, 2017) {
    var g = """
        .#.
        ..#
        ###
    """.trimIndent().lines().asGrid { it == '#' }

    val match = hashMapOf<BooleanGrid, BooleanGrid>()

    for (l in inputLines) {
        val (from, to) = l.split(" => ").map { it.split("/").asGrid { c -> c == '#' } }
        from.orientations().forEach { match[it] = to }
    }

    fun iter() {
        val w = if (g.width % 2 == 0) 2 else 3

        val parts = g.width / w
        val new = (w + 1) * parts
        val res = mutableBooleanGrid(new, new)

        for (wx in 0..<parts) for (wy in 0..<parts) {
            val start = Point(wx * w, wy * w)
            val m = g.subGrid(start, w, w)
            val matched = match.getValue(m)

            val end = Point(wx * (w + 1), wy * (w + 1))
            for (p in matched.pointsSequence) if (matched[p]) res[end + p] = true
        }

        g = res
    }

    repeat(5) { iter() }
    partOne = g.countTrue()
    repeat(18 - 5) { iter() }
    partTwo = g.countTrue()
}