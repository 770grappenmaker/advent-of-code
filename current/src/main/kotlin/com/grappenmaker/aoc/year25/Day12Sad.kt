package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(12, 2025) {
    val parts = rawInput.split("\n\n")
    val gs = parts.dropLast(1).map { it.countContains('#') }

    partOne = parts.last().lines().count { p ->
        val (s, ns) = p.split(": ")
        s.split('x').map(String::toInt).product() >= ns.split(" ").map(String::toInt).sumOfIndexed { idx, v -> v * gs[idx] }
    }

    partTwo = "This problem is weird and stupid... anyway Merry (early) Christmas!"
}