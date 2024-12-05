@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import java.util.Collections.swap

fun PuzzleSet.day05() = puzzle(day = 5) {
    val (fp, sp) = input.doubleLines()
    val assoc = Array(100) { mutableListOf<Int>() }
    for (l in fp.lines()) {
        val (a, b) = l.split('|').map { it.toInt() }
        assoc[b] += a
    }

    var p1 = 0
    var p2 = 0

    for (l in sp.lines()) {
        val mut = l.split(',').mapTo(mutableListOf()) { it.toInt() }
        var broken = false

        outer@ while (true) {
            for ((idx, i) in mut.withIndex()) for (t in assoc[i]) {
                val otherIdx = mut.indexOf(t)
                if (otherIdx < idx) continue

                broken = true
                swap(mut, idx, otherIdx)
                continue@outer
            }

            val ans = mut[mut.size / 2]
            if (broken) p2 += ans else p1 += ans

            break
        }
    }

    partOne = p1
    partTwo = p2
}