package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day03() = puzzle(day = 3) {
    val nums = inputLines.map { it.toList() }
    val gamma = nums.swapOrder().map { it.mostFrequent() }.joinToString("").toInt(2)
    val eps = gamma.inv() and 0b111111111111
    partOne = gamma.toLong() * eps.toLong()

    val ordering = compareBy<Map.Entry<Char, Int>> { it.value }.thenBy { it.key }
    fun proc(b: Boolean): Int {
        var curr = nums
        var p = 0

        while (curr.size > 1) {
            val m = curr.map { it[p] }.frequencies().maxWith(ordering).key
            curr = curr.filter { l -> (m == l[p]) == b }
            p++
        }

        return curr.single().joinToString("").toInt(2)
    }

    partTwo = proc(true) * proc(false)
}