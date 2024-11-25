@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year22

import com.grappenmaker.aoc.*

fun PuzzleSet.day05() = puzzle(day = 5) {
    val (start, insns) = rawInput.doubleLines()
    val init = start.lines().dropLast(1)
        .map { it.padEnd(35, ' ').slice(1..<35 step 4).toList() }.swapOrder()
        .map { l -> l.dropWhile { it == ' ' }.asReversed() }

    fun solve(part: Boolean): String {
        val curr = init.map(::ArrayDeque)

        for (l in insns.lines()) {
            val (_, c, _, f, _, t) = l.split(" ")
            curr[t.toInt() - 1].addAll(curr[f.toInt() - 1].removeLastN(c.toInt())
                .let { if (part) it else it.asReversed() })
        }

        return curr.joinToString("") { it.last().toString() }
    }

    partOne = solve(false)
    partTwo = solve(true)
}