package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.toRange

fun main() = simplePuzzle(2, 2025) {
    fun solve(range: (String) -> IntRange): Long {
        var ans = 0L

        for (p in input.split(",")) outer@ for (i in p.split("-").map { it.toLong() }.asPair().toRange()) {
            val check = i.toString()

            inner@ for (l in range(check)) {
                if (check.length % l != 0) continue

                val part = check.take(l)
                var left = check.drop(l)

                while (left.isNotEmpty()) {
                    if (!left.startsWith(part)) continue@inner
                    left = left.drop(part.length)
                }

                ans += i
                continue@outer
            }
        }

        return ans
    }

    partOne = solve { s -> if (s.length % 2 != 0) 0..<0 else (s.length / 2).let { it..it } }
    partTwo = solve { 1..(it.length / 2) }
}