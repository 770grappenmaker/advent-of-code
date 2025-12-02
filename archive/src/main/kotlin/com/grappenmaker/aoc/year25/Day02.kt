package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(2, 2025) {
    fun solve(range: (String) -> IntRange) = input.split(",").sumOf { p ->
        p.split("-").map { it.toLong() }.asPair().toRange().asSequence().filter { i ->
            i.toString().let { c -> range(c).any { c.length % it == 0 && c.chunkedSequence(it).allIdentical() } }
        }.sum()
    }

    partOne = solve { s -> if (s.length % 2 != 0) 0..<0 else (s.length / 2).let { it..it } }
    partTwo = solve { 1..(it.length / 2) }
}