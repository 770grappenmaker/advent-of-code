@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue

@PuzzleEntry
fun PuzzleSet.day20() = puzzle(day = 20) {
    val g = inputLines.asCharGrid()
    val start = g.findPointsValued('S').single()
    val end = g.findPointsValued('E').single()

    fun dist(from: Point): Map<Point, Int> {
        val queue = ArrayDeque<Pair<Point, Int>>()
        val seen = hashSetOf(from)
        queue += from to 0

        val res = mutableMapOf<Point, Int>()
        while (queue.isNotEmpty()) {
            val (curr, d) = queue.removeLast()
            res[curr] = minOf(d, res.getOrPut(curr) { Int.MAX_VALUE })
            with(g) {
                curr.adjacentSides().filter { g[it] != '#' && seen.add(it) }.forEach { queue.addFirst(it to d + 1) }
            }
        }

        return res
    }

    val fromDist = dist(start)
    val toDist = dist(end)
    val baseline = fromDist.gv(end)

    suspend fun solve(maxDist: Int) = g.pointsSequence.filter { g[it] != '#' }.asIterable().parallelize { p ->
        var ans = 0

        for (dx in -maxDist..maxDist) {
            val abs = dx.absoluteValue
            for (dy in abs - maxDist..maxDist - abs) {
                val np = Point(p.x + dx, p.y + dy)
                if (np !in g || g[np] == '#') continue

                val dist = fromDist.gv(p) + (p manhattanDistanceTo np) + toDist.gv(np)
                if (dist <= baseline - 100) ans++
            }
        }

        ans
    }.sum()

    runBlocking {
        partOne = solve(2)
        partTwo = solve(20)
    }
}