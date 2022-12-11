package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year22.parseRange

fun main() = simplePuzzle(4, 2019) {
    val range = input.parseRange()
    fun solve(partTwo: Boolean) = range.count { p ->
        val digits = p.toDigits()
        digits.windowed(2).filterIndexed { idx, (a, b) ->
            a == b && (!partTwo || digits.getOrNull(idx - 1) != a && digits.getOrNull(idx + 2) != b)
        }.any() && digits.sortedDescending() == digits
    }.s()

    partOne = solve(false)
    partTwo = solve(true)
}

fun Int.toDigits(base: Int = 10): List<Int> = sequence {
    var n = this@toDigits
    require(n >= 0)
    while (n != 0) {
        yield(n % base)
        n /= base
    }
}.toList()