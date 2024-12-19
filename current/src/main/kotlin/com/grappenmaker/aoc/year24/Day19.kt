@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day19() = puzzle(day = 19) {
    val (fp, sp) = input.doubleLines()
    val poss = fp.split(", ")

    fun solve(cast: (Long) -> Long): Long {
        val memo = hashMapOf<String, Long>()
        return sp.lines().sumOf { l ->
            fun test(left: String): Long = if (left.isEmpty()) 1 else memo.getOrPut(left) {
                cast(poss.filter { test -> left.startsWith(test) }.sumOf { test(left.drop(it.length)) })
            }

            test(l)
        }
    }

    partOne = solve { if (it > 0) 1 else 0 }
    partTwo = solve { it }
}