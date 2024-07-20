package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day20() = puzzle(20) {
    // Dang that is a stupid algorithm
    val num = input.toInt()
    val n10 = num / 10
    val list = MutableList(n10) { 0 }
    fun solve() = list.withIndex().filter { it.value >= num }.minOf { it.index + 1 }.s()

    (1..n10).forEach { i -> (i..n10 step i).forEach { list[it - 1] += i * 10 } }
    partOne = solve()

    list.replaceAll { 0 }
    (1..n10).forEach { i -> (i..n10 step i).take(50).forEach { list[it - 1] += i * 11 } }
    partTwo = solve()
}