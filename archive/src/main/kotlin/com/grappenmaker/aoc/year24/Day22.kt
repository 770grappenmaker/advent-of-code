@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day22() = puzzle(day = 22) {
    val ctr = hashMapOf<List<Long>, Long>().withDefault { 0 }
    var p1 = 0L

    for (l in inputLines) {
        val nums = generateSequence(l.toLong()) { curr ->
            var n = curr
            n = ((n shl 6) xor n) % 16777216L
            n = ((n ushr 5) xor n) % 16777216L
            n = ((n shl 11) xor n) % 16777216L
            n
        }.take(2001).toList()

        p1 += nums.last()

        val seen = hashSetOf<List<Long>>()
        nums.asSequence().map { it % 10 }.zipWithNext { a, b -> a - b }.windowed(4).forEachIndexed { idx, ss ->
            if (seen.add(ss)) ctr[ss] = ctr.gv(ss) + (nums[idx + 4] % 10)
        }
    }

    partOne = p1
    partTwo = ctr.values.max()
}