@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*

fun PuzzleSet.day12() = puzzle(day = 12) {
    val g = inputLines.asCharGrid()
    val todo = g.points.toHashSet()
    val regions = mutableListOf<Set<Point>>()

    while (todo.isNotEmpty()) {
        val curr = todo.first()
        todo -= curr

        val ans = hashSetOf(curr)
        val queue = ArrayDeque<Point>()
        queue += curr

        while (queue.isNotEmpty()) {
            val next = queue.removeLast()
            with(g) {
                for (adj in next.adjacentSides()) {
                    if (g[adj] != g[curr] || adj !in todo || !ans.add(adj)) continue

                    queue.addFirst(adj)
                    todo -= adj
                }
            }
        }

        regions += ans
    }

    fun solve(perim: (Set<Point>) -> Int) = regions.sumOf { pts -> pts.size * perim(pts) }

    partOne = solve { pts -> pts.sumOf { p -> p.adjacentSidesInf().count { it !in pts } } }
    partTwo = solve { pts ->
        val lookup = List(4) { hashSetOf<Point>() }

        for (p in pts) for (d in Direction.entries) {
            val test = p + d
            if (test !in pts) lookup[d.ordinal] += test
        }

        var ans = 0

        for ((d, same) in lookup.withIndex()) {
            val seen = hashSetOf<Point>()
            val dir = Direction.entries[d]
            val tds = listOf(dir.next(1), dir.next(-1))

            for (p in same) {
                if (p in seen) continue
                ans++

                val queue = ArrayDeque<Point>()
                queue += p

                while (queue.isNotEmpty()) {
                    val curr = queue.removeLast()

                    for (td in tds) {
                        val next = curr + td
                        if (next in same && seen.add(next)) queue.addFirst(next)
                    }
                }
            }
        }

        ans
    }
}