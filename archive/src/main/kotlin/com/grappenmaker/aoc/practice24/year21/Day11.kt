package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day11() = puzzle(day = 11) {
    val g = inputLines.asMutableDigitGrid()
    with (g) {
        var ans = 0

        for (i in counter()) {
            mapInPlace { it + 1 }
            val flashed = hashSetOf<Point>()
            val consider = ArrayDeque(points)

            while (consider.isNotEmpty()) {
                val curr = consider.removeLast()
                if (g[curr] <= 9 || !flashed.add(curr)) continue

                ans++
                for (p in curr.allAdjacent()) {
                    g[p] += 1
                    if (p !in flashed && g[p] > 9) consider.addFirst(p)
                }
            }

            flashed.forEach { g[it] = 0 }
            if (i == 100) partOne = ans
            if (flashed.size == g.size) {
                partTwo = i
                break
            }
        }
    }
}