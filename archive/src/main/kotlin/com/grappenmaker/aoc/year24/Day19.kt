@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*

fun PuzzleSet.day19() = puzzle(day = 19) {
    val (fp, sp) = input.doubleLines()
    val poss = fp.split(", ")

    val memo = hashMapOf<String, Long>()
    fun solve(cast: (Long) -> Long) = sp.lines().sumOf { l ->
        fun test(left: String): Long = if (left.isEmpty()) 1 else cast(memo.getOrPut(left) {
            poss.filter { left.startsWith(it) }.sumOf { test(left.drop(it.length)) }
        })

        test(l)
    }

    partTwo = solve { it }
    partOne = solve { if (it > 0) 1 else 0 }
}