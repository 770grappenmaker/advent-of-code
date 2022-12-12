package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day12() = puzzle {
    val charGrid = inputLines.asCharGrid()
    val start = charGrid.points.first { charGrid[it] == 'S' }
    val end = charGrid.points.first { charGrid[it] == 'E' }

    val grid = inputLines.asGrid {
        when (it) {
            'S' -> 0
            'E' -> 25
            else -> it - 'a'
        }
    }

    with(grid) {
        fun eval(start: Point) = dijkstra(
            start,
            isEnd = { it == end },
            neighbors = { it.adjacentSides().filter { p -> this[p] - this[it] <= 1 } },
            findCost = { 1 }
        )?.cost

        // Slightly faster
//        fun eval(start: Point): Int? {
//            val queue = queueOf(SearchNodeC(start, 0))
//            val seen = hashSetOf(start)
//            while (queue.isNotEmpty()) {
//                val (p, c) = queue.removeLast()
//                if (p == end) return c
//
//                p.adjacentSides().filter { p2 -> this[p2] - this[p] <= 1 }.forEach {
//                    if (seen.add(it)) queue.addFirst(SearchNodeC(it, c + 1))
//                }
//            }
//
//            return null
//        }

        partOne = eval(start).s()
        partTwo = pointsSequence.filter { this[it] == 0 }.mapNotNull { eval(it) }.min().s()
    }
}