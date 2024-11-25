package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.*
import java.util.PriorityQueue

fun PuzzleSet.day09() = puzzle(day = 9) {
    val g = inputLines.asDigitGrid()
    with(g) {
        partOne = g.points.filter { it.adjacentSides().all { p -> g[p] > g[it] } }.sumOf { g[it] + 1 }

        val left = g.points.filterTo(hashSetOf()) { g[it] < 9 }
        val ans = PriorityQueue<Int>()

        while (left.isNotEmpty()) {
            val start = left.first()
            left -= start

            val queue = ArrayDeque<Point>()
            val seen = hashSetOf(start)
            queue += start

            var res = 0
            while (queue.isNotEmpty()) {
                val curr = queue.removeLast()
                res++

                for (p in curr.adjacentSides()) if (p in left && seen.add(p)) {
                    queue.addFirst(p)
                    left.remove(p)
                }
            }

            ans += res
            if (ans.size > 3) ans.remove()
        }

        partTwo = ans.product()
    }
}