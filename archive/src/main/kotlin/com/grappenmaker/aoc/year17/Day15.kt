package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    val (startA, startB) = input.splitInts().map(Int::toLong)
    fun seq(start: Long, mul: Long, div: Long = 1L) = generateSequence(start) { it * mul % Int.MAX_VALUE.toLong() }
        .drop(1).let { s -> if (div != 1L) s.filter { it % div == 0L } else s }

    fun solve(a: Sequence<Long>, b: Sequence<Long>, count: Int) = a.zip(b).take(count)
        .count { (a, b) -> (a and 0xFFFFL) == (b and 0xFFFFL) }.s()

    partOne = solve(seq(startA, 16807L), seq(startB, 48271L), 40000000)
    partTwo = solve(seq(startA, 16807L, 4), seq(startB, 48271L, 8), 5000000)
}