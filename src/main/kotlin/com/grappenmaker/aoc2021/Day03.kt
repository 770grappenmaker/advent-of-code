package com.grappenmaker.aoc2021

import kotlin.math.pow

fun solveDay3() {
    // Part one
    val lines = getInputLines(3)
    val width = lines.first().length
    val numbers = lines.map { it.toInt(2) }

    val gamma = (0 until width)
        .reduceIndexed { idx, acc, v -> acc or (getMostCommon(width, v, numbers) shl width - idx - 1) }
    val epsilon = gamma.inv() and 2.0.pow(width).toInt() - 1

    println("Part one: ${gamma to epsilon} -> ${gamma * epsilon}")

    // Part two
    val findRating = { doNegate: Boolean ->
        var result = numbers

        for (i in 0 until width) {
            val toCheck = getMostCommon(width, i, result)
            result = result.filter { it.getBit(width - i - 1) == if (doNegate) toCheck.inv() and 1 else toCheck }

            if (result.size == 1) break
        }

        result.first()
    }

    val oxygenRating = findRating(false)
    val co2ScrubRating = findRating(true)

    println("Part two: ${oxygenRating to co2ScrubRating} -> ${oxygenRating * co2ScrubRating}")
}

fun Int.getBit(index: Int) = this shr index and 1

fun getMostCommon(width: Int, idx: Int, values: List<Int>): Int {
    val sum = values.sumOf { b -> (if (b.getBit(width - idx - 1) == 1) 1 else -1).toInt() }
    return if (sum >= 0) 1 else 0
}