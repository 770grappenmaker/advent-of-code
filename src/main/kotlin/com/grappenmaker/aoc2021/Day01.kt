package com.grappenmaker.aoc2021

fun Solution.solveDay1(): Pair<Int, Int> {
    // Part one
    val input = inputLines.map { it.toInt() }
    val countIncrements = { arr: List<Int> -> arr.asSequence().drop(1).filterIndexed { i, n -> n > arr[i] }.count() }
    val increments = countIncrements(input)

    // Part two
    val sumIncrements = countIncrements(input.windowed(3).map { it.sum() })
    return increments to sumIncrements
}