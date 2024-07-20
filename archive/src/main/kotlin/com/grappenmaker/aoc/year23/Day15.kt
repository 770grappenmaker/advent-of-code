package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    fun String.hash() = fold(0) { a, ch -> ((a + ch.code) * 17) % 256 }
    val parts = input.split(",")
    partOne = parts.sumOf { it.hash() }.s()

    val state = MutableList(256) { linkedMapOf<String, Int>() }
    parts.forEach { p ->
        val lbl = p.takeWhile { it.isLetter() }
        val b = lbl.hash()
        when (p[lbl.length]) {
            '-' -> state[b] -= lbl
            '=' -> state[b][lbl] = p.last().digitToInt()
        }
    }

    partTwo = state.sumOfIndexed { bi, b -> b.toList().sumOfIndexed { li, (_, f) -> (bi + 1) * (li + 1) * f } }.s()
}