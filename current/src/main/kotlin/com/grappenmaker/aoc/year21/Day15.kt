@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import java.util.ArrayDeque
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day15() = puzzle(day = 15) {
    with(inputLines.asDigitGrid()) {
        val start = Point(0, 0)
        val earlyEnd = bottomRightCorner
        val end = Point(width * 5 - 1, height * 5 - 1)
        val bounds = Rectangle(start, end)

        data class Entry(val cost: Int, val pos: Point)

        val queue = PriorityQueue<Entry>(compareBy { it.cost })
        queue += Entry(0, start)
        val seen = hashSetOf<Point>()

        var p1 = false

        while (queue.isNotEmpty()) {
            val curr = queue.remove()
            if (!p1 && curr.pos == earlyEnd) {
                partOne = curr.cost
                p1 = true
            }

            if (curr.pos == end) {
                partTwo = curr.cost
                break
            }

            for (adj in curr.pos.adjacentSidesInf()) {
                if (adj !in bounds) continue
                if (!seen.add(adj)) continue

                val tot = this[Point(adj.x % width, adj.y % height)] + adj.x / width + adj.y / height
                queue.add(Entry(curr.cost + if (tot > 9) tot % 10 + 1 else tot, adj))
            }
        }
    }
}