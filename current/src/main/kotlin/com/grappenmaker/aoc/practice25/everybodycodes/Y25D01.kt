package com.grappenmaker.aoc.practice25.everybodycodes

import java.util.*

fun ECSolveContext.y25day01() {
    fun ECInput.solve(last: Boolean = false): String {
        val (p, _, ins) = inputLines
        val parts = p.split(",").toMutableList()
        var ptr = 0

        for (i in ins.split(",")) {
            val a = i.drop(1).toInt()
            val nidx = when (i.take(1)) {
                "L" -> (ptr - a).mod(parts.size)
                "R" -> (ptr + a).mod(parts.size)
                else -> error("Impossible")
            }

            if (last) Collections.swap(parts, ptr, nidx) else ptr = nidx
        }

        return parts[ptr]
    }

    partOne = partOneInput.solve()
    partTwo = partTwoInput.solve()
    partThree = partThreeInput.solve()
}