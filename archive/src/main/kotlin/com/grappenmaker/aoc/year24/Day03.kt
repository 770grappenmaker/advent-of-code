@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day03() = puzzle(day = 3) {
//    var partOne by overwritePartOne(0L)
//    var partTwo by overwritePartTwo(0L)
    var p1 = 0L
    var p2 = 0L

    var on = true
    var idx = 0

    while (idx in input.indices) {
        if (input.startsWith("do()", idx)) {
            on = true
            idx += 4
            continue
        }

        if (input.startsWith("don't()", idx)) {
            on = false
            idx += 7
            continue
        }

        if (!input.startsWith("mul(", idx++)) continue
        idx += 3

        val n1 = input.drop(idx).takeWhile { it.isDigit() }.also { idx += it.length }
        if (input[idx++] != ',') continue

        val n2 = input.drop(idx).takeWhile { it.isDigit() }.also { idx += it.length }
        if (input[idx++] != ')') continue
        if (n1.length !in 1..3 || n2.length !in 1..3) continue

        val m = n1.toLong() * n2.toLong()
        p1 += m
        if (on) p2 += m
    }

    partOne = p1
    partTwo = p2
}