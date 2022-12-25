package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.swapOrder

fun PuzzleSet.day3() = puzzle {
    val inp = inputLines.map { it.trim().split(" +".toRegex()).map(String::toInt) }
    fun List<List<Int>>.solve() = map { it.sorted() }.count { (a, b, c) -> a + b > c }.s()
    partOne = inp.solve()
    partTwo = inp.swapOrder().flatMap { it.chunked(3) }.solve()
}