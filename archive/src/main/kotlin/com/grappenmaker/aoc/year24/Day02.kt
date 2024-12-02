@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.absoluteValue

fun PuzzleSet.day02() = puzzle(day = 2) {
    val g = inputLines.map { l -> l.split(" ").map { it.toInt() } }

    fun List<Int>.test(): Boolean {
        val diff = windowed(2) { (a, b) -> a - b }
        return (diff.all { it < 0 } || diff.all { it > 0 }) && diff.all { it.absoluteValue in 1..3 }
    }

    fun solve(p2: Boolean) = g.count { l ->
        (l.test() || p2 && l.indices.any {
            l.toMutableList().run {
                removeAt(it)
                test()
            }
        })
    }

    partOne = solve(false)
    partTwo = solve(true)
}