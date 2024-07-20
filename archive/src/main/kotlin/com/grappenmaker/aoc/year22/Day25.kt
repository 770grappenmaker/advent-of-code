package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day25() = puzzle { partOne = inputLines.sumOf { it.toSnafuLong() }.toSnafuString() }

fun String.toSnafuLong() = deepen().asReversed().foldIndexed(0L) { idx, acc, curr ->
    acc + when (curr) {
        '-' -> -1
        '=' -> -2
        else -> curr.digitToInt().toLong()
    } * fivePow(idx)
}

fun Long.toSnafuString() = buildString {
    var curr = this@toSnafuString
    while (curr != 0L) {
        append(when(val v = curr % 5) {
            4L -> '-'
            3L -> '='
            else -> v.toInt().digitToChar()
        })

        curr = (curr + 2) / 5
    }
}.reversed()

fun fivePow(n: Int): Long {
    var result = 1L
    repeat(n) { result *= 5 }
    return result
}