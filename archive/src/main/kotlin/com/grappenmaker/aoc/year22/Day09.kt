package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day9() = puzzle {
    val insns = inputLines.map { it.split(" ") }.map { (d, a) -> d.single().parseDirection() to a.toInt() }

    fun solve(n: Int): String {
        val seen = hashSetOf(Point(0, 0))
        val nodes = MutableList(n) { Point(0, 0) }

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