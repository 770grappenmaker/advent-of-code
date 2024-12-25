@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day10() = puzzle(day = 10) {
    val g = inputLines.asDigitGrid()
    val th = g.findPointsValued(0)

    fun <T> solve(initial: (Point) -> T, gen: (T, Point) -> T, extr: (T) -> Point) = th.sumOf { p ->
        val queue = ArrayDeque<T>()
        queue += initial(p)
        val seen = hashSetOf(queue.first())
        var ans = 0

        while (queue.isNotEmpty()) {
            val curr = queue.removeLast()
            val pt = extr(curr)

            if (g[pt] == 9) {
                ans++
                continue
            }

            with (g) {
                pt.adjacentSides()
                    .filter { (g[it] - g[pt]) == 1 }
                    .map { gen(curr, it) }
                    .forEach { if (seen.add(it)) queue.addFirst(it) }
            }
        }

        ans
    }

    partOne = solve(initial = { it }, gen = { _, p -> p }, extr = { it })

    data class Node(val curr: Point, val path: Set<Point>)
    partTwo = solve(initial = { Node(it, setOf(it)) }, gen = { curr, p -> Node(p, curr.path + p) }, extr = { it.curr })
}