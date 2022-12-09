package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction.*

// Naughty code
fun PuzzleSet.day9() = puzzle {
    val insns = inputLines.map { it.split(" ") }.map { (d, a) ->
        when (d) {
            "U" -> UP
            "D" -> DOWN
            "L" -> LEFT
            "R" -> RIGHT
            else -> error("No input")
        } to a.toInt()
    }

    fun solve(n: Int): String {
        val seen = hashSetOf(Point(0, 0))
        val nodes = buildListN(n) { Point(0, 0) }.toMutableList()

        insns.forEach { (d, a) ->
            repeat(a) {
                nodes.mapInPlaceIndexed { idx, point ->
                    if (idx == 0) point + d
                    else {
                        val before = nodes[idx - 1]
                        if (!point.adjacentTo(before)) point + (before - point).clampUnit() else point
                    }
                }

                seen.add(nodes.last())
            }
        }

        return seen.size.s()
    }

    partOne = solve(2)
    partTwo = solve(10)
}