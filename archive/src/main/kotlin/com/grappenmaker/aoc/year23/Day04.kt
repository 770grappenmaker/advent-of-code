package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day4() = puzzle(day = 4) {
    val p = inputLines.map { l ->
        val (w, y) = l.substringAfter(": ").split(" | ").map { s ->
            s.split(" ").filter { it.isNotEmpty() }.mapTo(hashSetOf()) { it.toInt() }
        }

        y.intersect(w).size
    }

    partOne = p.sumOf { c -> if (c == 0) 0 else 2.pow(c - 1) }.s()

    val res = MutableList(p.size) { 1 }
    p.forEachIndexed { idx, c -> (idx + 1..idx + c).forEach { res[it] += res[idx] } }
    partTwo = res.sum().s()
}