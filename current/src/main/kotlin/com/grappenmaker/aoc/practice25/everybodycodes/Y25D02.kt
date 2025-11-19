package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*
import kotlin.math.absoluteValue

fun ECSolveContext.y25day02() {
    fun Pair<Long, Long>.sum(b: Pair<Long, Long>): Pair<Long, Long> {
        return (first + b.first) to (second + b.second)
    }

    fun Pair<Long, Long>.div(b: Pair<Long, Long>): Pair<Long, Long> {
        return (first / b.first) to (second / b.second)
    }

    fun Pair<Long, Long>.mul(b: Pair<Long, Long>): Pair<Long, Long> {
        return (first * b.first - second * b.second) to (first * b.second + second * b.first)
    }

    fun compute(p: Pair<Long, Long>, repeat: Int, div: Pair<Long, Long>): Pair<Boolean, Pair<Long, Long>> {
        var ans = 0L to 0L

        repeat(repeat) {
            ans = ans.mul(ans)
            ans = ans.div(div)
            ans = ans.sum(p)

            if (ans.first.absoluteValue > 1000000) return false to ans
            if (ans.second.absoluteValue > 1000000) return false to ans
        }

        return true to ans
    }

    fun ECInput.parse() = input.ints().map { it.toLong() }.let { (x, y) -> x to y }

    fun ECInput.solveLate(fine: Long): Int {
        val start = parse()
        var ans = 0

        for (dx in 0L..1000L / fine) for (dy in 0L..1000L / fine) {
            val p = start.sum((fine * dx) to (fine * dy))
            if (compute(p, 100, 100000L to 100000L).first) ans++
        }

        return ans
    }

    partOne = compute(partOneInput.parse(), 3, 1000L to 1000L).second
    partTwo = partTwoInput.solveLate(10)
    partThree = partThreeInput.solveLate(1)
}