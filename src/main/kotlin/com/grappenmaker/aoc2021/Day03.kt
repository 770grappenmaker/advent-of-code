package com.grappenmaker.aoc2021

val negate = { num: Int -> if (num >= 1) 0 else 1 }

fun solveDay3() {
    // Part one
    val lines = getInputLines(3)
    val indices = lines.first().indices
    val numbers = lines.map { it.toCharArray().map { c -> if (c == '1') 1 else 0 } }

    val mostCommon = indices.map { getMostCommon(it, numbers) }
    val leastCommon = mostCommon.map(negate)

    val gamma = mostCommon.foldBits()
    val epsilon = leastCommon.foldBits()

    println("Part one: ${gamma to epsilon} -> ${gamma * epsilon}")

    // Part two
    val findRating = { doNegate: Boolean ->
        var result = numbers

        for (i in indices) {
            val toCheck = getMostCommon(i, result)
            result = result.filter { it[i] == if (doNegate) negate(toCheck) else toCheck }

            if (result.size == 1) break
        }

        result.first().foldBits()
    }

    val oxygenRating = findRating(false)
    val co2SrubRating = findRating(true)

    println("Part two: ${oxygenRating to co2SrubRating} -> ${oxygenRating * co2SrubRating}")
}

fun getMostCommon(idx: Int, values: List<List<Int>>): Int {
    val sum = values.sumOf { b -> (if (b[idx] == 1) 1 else -1).toInt() }
    return if (sum >= 0) 1 else 0
}

fun Iterable<Int>.foldBits() =
    foldIndexed(0) { idx: Int, acc: Int, v: Int -> acc or (v shl count() - idx - 1) }