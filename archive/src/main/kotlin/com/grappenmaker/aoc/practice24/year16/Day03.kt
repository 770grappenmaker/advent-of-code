package com.grappenmaker.aoc.practice24.year16

import com.grappenmaker.aoc.*

fun PuzzleSet.day03() = puzzle(day = 3) {
    val nums = inputLines.asSequence().map { it.ints().asSequence() }
    fun Sequence<Sequence<Int>>.solve() = flatten().chunked(3) { it.sorted() }.count { (a, b, c) -> a + b > c }

    partOne = nums.solve()
    partTwo = nums.transpose().solve()
}