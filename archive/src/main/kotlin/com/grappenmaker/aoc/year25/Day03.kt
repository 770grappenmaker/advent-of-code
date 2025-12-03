package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.simplePuzzle

fun main() = simplePuzzle(3, 2025) {
    fun solve(n: Int) = inputLines.sumOf { l ->
        (n downTo 1).fold(l.map { it.digitToInt() } to 0L) { (digits, soFar), left ->
            val nextIdx = (0..digits.size - left).maxBy { digits[it] }
            digits.drop(nextIdx + 1) to 10 * soFar + digits[nextIdx]
        }.second
    }

    partOne = solve(2)
    partTwo = solve(12)
}