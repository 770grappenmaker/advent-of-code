package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(6, 2023) {
    fun String.solve(): Long {
        val (t, d) = lines().map { it.longs() }
        var ans = 1L

        for (i in t.indices) {
            var part = 0
            val tt = t[i]
            val dd = d[i]

            for (j in 1..<tt) {
                val td = (tt - j) * j
                if (td > dd) part ++
            }

            ans *= part
        }

        return ans
    }

    partOne = input.solve()
    partTwo = input.filter { it != ' ' }.solve()
}
