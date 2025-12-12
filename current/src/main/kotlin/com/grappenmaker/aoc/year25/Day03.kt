package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day03() = puzzle(day = 3) {
    fun solve(n: Int) = inputLines.sumOf { l ->
        (n downTo 1).fold(l.map { it.digitToInt() } to 0L) { (digits, soFar), left ->
            val nextIdx = (0..digits.size - left).maxBy { digits[it] }
            digits.subList(nextIdx + 1, digits.size) to 10 * soFar + digits[nextIdx]
        }.second
    }

    partOne = solve(2)
    partTwo = solve(12)
}