package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.swapOrder

@PuzzleEntry
fun PuzzleSet.day3() = puzzle {
    val inp = inputLines.map { it.trim().split(" +".toRegex()).map(String::toInt) }
    fun List<List<Int>>.solve() = map { it.sorted() }.count { (a, b, c) -> a + b > c }.toString()
    partOne = inp.solve()
    partTwo = inp.swapOrder().flatMap { it.chunked(3) }.solve()
}