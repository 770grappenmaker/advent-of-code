package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.floodFill
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(day = 12) {
    val graph = inputLines.flatMap { l ->
        val (from, toList) = l.split(" <-> ")
        toList.split(", ").flatMap { listOf(from to it, it to from) }
    }.groupBy { (k) -> k }.mapValues { (_, t) -> t.map { it.second } }

    fun String.calc() = floodFill(this, neighbors = { graph[it] ?: emptyList() })
    partOne = "0".calc().size.s()

    // optimized but weird
    val seen = hashSetOf<String>()
    partTwo = graph.keys.mapNotNull { if (it in seen) null else it.calc().also { f -> seen += f } }.size.s()

    // unoptimized
    // partTwo = graph.keys.map { it.calc() }.distinct().size.s()
}