@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*

fun PuzzleSet.day22() = puzzle(day = 22) {
    val ctr = hashMapOf<Int, Int>().withDefault { 0 }
    var p1 = 0L

    for (l in inputLines) {
        val seen = hashSetOf<Int>()
        var n = l.toInt()
        var last = n % 10
        var key = 0

        repeat(2000) {
            n = ((n shl 6) xor n) and 0xffffff
            n = ((n shr 5) xor n) and 0xffffff
            n = ((n shl 11) xor n) and 0xffffff

            val mod = n % 10
            key = key shl 8
            key = key or (mod - last + 9)
            last = mod
            if (seen.add(key)) ctr[key] = ctr.gv(key) + mod
        }

        p1 += n
    }

    partOne = p1
    partTwo = ctr.values.max()
}