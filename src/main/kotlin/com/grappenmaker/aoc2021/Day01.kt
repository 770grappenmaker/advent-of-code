package com.grappenmaker.aoc2021

fun solveDay1() {
    // Part one
    val input = getInputLines(1).map { it.toInt() }
    val countIncrements = { arr: List<Int> -> arr.asSequence().drop(1).filterIndexed { i, n -> n > arr[i] }.count() }

    val increments = countIncrements(input)
    println("Part one: $increments")

    // Part two
    val sumIncrements = countIncrements(input.windowed(3).map { it.sum() })
    println("Part two: $sumIncrements")
}