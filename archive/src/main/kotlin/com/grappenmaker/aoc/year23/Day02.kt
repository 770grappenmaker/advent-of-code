package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allIndexed
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.product

@PuzzleEntry
fun PuzzleSet.day2() = puzzle(day = 2) {
    val g = inputLines.map { l ->
        l.substringAfter(": ").split("; ").map { p ->
            p.split(", ").associate { s ->
                val (v, c) = s.split(" ")
                c to v.toInt()
            }.withDefault { 0 }
        }
    }

    val c = listOf("red", "green", "blue")
    partOne = g.withIndex().filter { (_, v) ->
        v.all { r -> c.allIndexed { idx, t -> r.getValue(t) <= idx + 12 } }
    }.sumOf { (i) -> i + 1 }.s()

    partTwo = g.sumOf { r -> c.map { r.maxOf { m -> m.getValue(it) } }.product() }.s()
}