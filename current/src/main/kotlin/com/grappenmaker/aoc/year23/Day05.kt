@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day05() = puzzle(day = 5) {
    val dd = input.doubleLines()
    val seeds = dd.first().split(" ").drop(1).map { it.toLong() }

    data class Mapping(val source: LongRange, val dest: Long)

    fun Mapping.mapRange(to: LongRange): Pair<LongRange?, List<LongRange>> {
        val overlap = source.overlap(to)
        val res = if (overlap != null) {
            val start = dest + (overlap.first - source.first)
            start..<start + overlap.width()
        } else null

        return res to to - source
    }

    val mappings = dd.drop(1).map { ll ->
        val lines = ll.lines()
        lines.drop(1).map { line ->
            val (d, s, l) = line.split(" ").map { it.toLong() }
            Mapping(s..<s+l, d)
        }
    }

    fun solve(ranges: List<LongRange>) = ranges.minOf { range ->
        var curr = listOf(range)

        for (mapping in mappings) {
            val next = mutableListOf<LongRange>()

            for (mm in mapping) {
                val nextTodo = mutableListOf<LongRange>()

                for (ran in curr) {
                    val (mapped, todo) = mm.mapRange(ran)
                    if (mapped != null) next.add(mapped)
                    nextTodo.addAll(todo)
                }

                curr = nextTodo
            }

            curr = (curr + next).simplify()
        }

        curr.minOf { it.first }
    }

    partOne = solve(seeds.map { it..it })
    partTwo = solve(seeds.chunked(2).map { (start, len) -> start..<start + len })
}