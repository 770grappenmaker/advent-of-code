package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day01() {
    fun solve(n: Int, input: ECInput): Int {
        var ans = 0

        for (c in input.input.toList().chunked(n)) {
            for (p in c) ans += when (p) {
                'B' -> 1
                'C' -> 3
                'D' -> 5
                else -> 0
            }

            val p = c.size - c.countContains('x')
            ans += p * (p - 1)
        }

        return ans
    }

    partOne = solve(1, partOneInput)
    partTwo = solve(2, partTwoInput)
    partThree = solve(3, partThreeInput)
}