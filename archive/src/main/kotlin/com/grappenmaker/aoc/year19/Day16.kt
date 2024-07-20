package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.absoluteValue

@PuzzleEntry
fun PuzzleSet.day16() = puzzle(day = 16) {
    fun List<Int>.solve() = take(8).joinToString("")

    val initialSignal = input.map(Char::digitToInt)
    val patterns = List(initialSignal.size) { idx ->
        buildList {
            repeat(idx + 1) { add(0) }
            repeat(idx + 1) { add(1) }
            repeat(idx + 1) { add(0) }
            repeat(idx + 1) { add(-1) }
        }.asSequence().repeatInfinitely().drop(1).take(initialSignal.size).toList()
    }

    partOne = generateSequence(initialSignal) { signal ->
        List(signal.size) { idx -> signal.zip(patterns[idx]).sumOf { (a, b) -> a * b }.absoluteValue % 10 }
    }.nth(100).solve()

    // The idea is that the offset is a 7-digit number and therefore probably sufficiently big to surpass
    // the first bit of the input, so we discard it. Then, since we are at a sufficiently large index,
    // and the + 0*x is useless, the idea is that we will never reach the *-1. Therefore, it is just a sum.
    // Since the sum is mod 10, and we need the answer of the last calculation in the next one, we can keep a rolling
    // sum. We only care about the first 8 digits of the answer, which is why we need to make sure we work backwards,
    // as we do not want to overwrite the first 8 digits with an incorrect sum. This is just an approximation.
    val offset = input.take(7).toInt()
    val current = initialSignal.repeat(10000).drop(offset).asReversed().toMutableList()

    // We can validate if this approximation is correct like this:
    require(offset > current.size / 2) { "Approximation invalid, cannot calculate" }
    repeat(100) {
        var rollingApproximateSum = 0
        current.mapInPlace { v ->
            rollingApproximateSum += v
            rollingApproximateSum %= 10
            rollingApproximateSum
        }
    }

    partTwo = current.asReversed().solve()
}