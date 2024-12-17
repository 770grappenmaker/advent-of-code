@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import java.util.*

fun PuzzleSet.day16() = puzzle(day = 16) {
    val g = inputLines.asCharGrid()
    val start = g.findPointsValued('S').single()
    val end = g.findPointsValued('E').single()

    data class Node(val pos: Point, val dir: Direction, val cost: Long = 0L)

    fun path(first: Node, rev: Boolean): Pair<Long, Map<Pair<Point, Direction>, Long>> {
        val queue = PriorityQueue<Node>(compareBy { it.cost })
        queue += first
        val seen = hashSetOf<Pair<Point, Direction>>()

        var bestCost = Long.MAX_VALUE
        val costs = hashMapOf<Pair<Point, Direction>, Long>()

        while (queue.isNotEmpty()) {
            val (pos, dir, cost) = queue.remove()

            val key = pos to dir
            if (key !in costs) costs[key] = cost

            if (pos == end) bestCost = minOf(bestCost, cost)
            if (!seen.add(pos to dir)) continue

            val nd = if (rev) pos - dir else pos + dir
            if (g[nd] != '#') queue.add(Node(nd, dir, cost + 1))
            queue.add(Node(pos, dir.next(-1), cost + 1000))
            queue.add(Node(pos, dir.next(1), cost + 1000))
        }

        return bestCost to costs
    }

    val (p1, forward) = path(Node(start, RIGHT), false)
    val (_, backward) = path(Node(end, UP), true)

    var ans = 0
    for (point in g.pointsSequence) for (dir in Direction.entries) {
        val key = point to dir
        if (key !in forward || key !in backward) continue

        val f = forward.gv(key)
        val b = backward.gv(key)
        if (f + b == p1) {
            ans++
            break
        }
    }

    partOne = p1
    partTwo = ans
}