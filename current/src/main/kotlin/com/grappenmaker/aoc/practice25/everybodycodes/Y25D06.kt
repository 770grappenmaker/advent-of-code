package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.countContains

fun ECSolveContext.y25day06() {
    fun solve(inp: String): Long {
        var ans = 0L

        for (ptr in inp.indices) {
            if (!inp[ptr].isLowerCase()) continue
            val c = inp[ptr].uppercaseChar()
            ans += inp.slice(0..<ptr).countContains(c)
        }

        return ans
    }

    partOne = solve(partOneInput.input.filter { it.lowercaseChar() == 'a' })
    partTwo = solve(partTwoInput.input)

    val inp = partThreeInput.input
    var ans = 0L

    for (ptr in inp.indices) {
        if (!inp[ptr].isLowerCase()) continue
        val c = inp[ptr].uppercaseChar()

        for (j in -1000..1000) {
            val off = ptr + j
            if (inp[off.mod(inp.length)] == c) ans += if (off in 0..<10000) 1000 else 999
        }
    }

    partThree = ans
}