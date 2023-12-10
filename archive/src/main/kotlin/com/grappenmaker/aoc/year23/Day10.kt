package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day10() = puzzle(day = 10) {
    val g = inputLines.asCharGrid()
    val m = mapOf(
        '|' to listOf(UP, DOWN),
        '-' to listOf(LEFT, RIGHT),
        'L' to listOf(UP, RIGHT),
        'J' to listOf(UP, LEFT),
        'F' to listOf(DOWN, RIGHT),
        '7' to listOf(DOWN, LEFT),
        '.' to emptyList(),
    )

    val s = g.findPointsValued('S').single()
    val dirs = Direction.entries.filter { d -> -d in m.getValue(g[s + d]) }
    val vert = m.filter { UP in it.value }.keys + listOfNotNull('S'.takeIf { UP in dirs })

    val d = fillDistance(s) { c ->
        when (val cc = g[c]) {
            'S' -> dirs
            in m -> m.getValue(cc)
            else -> error("Impossible")
        }.map { c + it }.filter { it in g && g[it] != '.' }
    }

    partOne = d.values.max().s()

    // get me out of here
    var n = 0
    var parity = Point(0, 0) in d
    g.pointsFlipped.forEach { p ->
        when {
            p in d -> if (g[p] in vert) parity = !parity
            parity -> n++
        }
    }

    partTwo = n.s()
}