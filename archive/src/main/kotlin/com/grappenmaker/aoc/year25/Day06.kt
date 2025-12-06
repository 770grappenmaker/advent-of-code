package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(6, 2025) {
    fun Sequence<List<Long>>.solve() = zip(inputLines.last().splitToSequence("\\s+".toRegex())).sumOf { (nums, op) ->
        nums.reduce(
            when (op) {
                "+" -> Long::plus
                "*" -> Long::times
                else -> error(op)
            }
        )
    }

    partOne = inputLines.dropLast(1).asSequence().map { it.split("\\s+".toRegex()) }
        .transpose(forceDrain = false)
        .map { l -> l.map { it.trim().toLong() } }.solve()

    partTwo = inputLines.dropLast(1).asSequence().map { it.toList() }
        .transpose(forceDrain = false)
        .split { l -> l.all { it == ' ' } }
        .map { l -> l.map { it.joinToString("").trim().toLong() } }.solve()
}