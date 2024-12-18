@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day18() = puzzle(day = 18) {
    val ps = inputLines.map { l ->
        val (x, y) = l.split(',').map { it.toInt() }
        Point(x, y)
    }

    fun test(ls: List<Point>): Int? {
        val g = ls.asBooleanGrid()

        val queue = ArrayDeque<Pair<Point, Int>>()
        queue += g.topLeftCorner to 0
        val seen = hashSetOf(g.topLeftCorner)
        val end = g.bottomRightCorner

        while (queue.isNotEmpty()) {
            val (curr, d) = queue.removeLast()
            if (curr == end) return d

            with (g) {
                for (s in curr.adjacentSides()) {
                    if (!g[s] && seen.add(s)) queue.addFirst(s to (d + 1))
                }
            }
        }

        return null
    }

    partOne = test(ps.take(1024)) ?: "Somehow no path"

    val (a, b) = ps[(1024..inputLines.size).first { test(ps.take(it)) == null } - 1]
    partTwo = "$a,$b"
}