package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day10() = puzzle {
    // Parse as digits (48 is the ascii code of zero)
    val digits = input.trim().map { it.code - 48 }
    val lasSequence = { start: List<Int> -> generateSequence(start) { it.applyLAS() }.drop(1) }

    // DRY folks
    // There is probably also a fancy mathematical
    // way of calculating this, but I am not aware. this works.
    val after40 = lasSequence(digits).take(40).last()
    val after50 = lasSequence(after40).take(10).last()
    partOne = after40.size.s()
    partTwo = after50.size.s()
}

fun List<Int>.applyLAS() = if (isEmpty()) emptyList() else buildList {
    var currentDigit = this@applyLAS.first()
    var currentCount = 1

    val push = {
        add(currentCount)
        add(currentDigit)
    }

    this@applyLAS.drop(1).forEach { digit ->
        if (currentDigit == digit) {
            currentCount++
        } else {
            push()
            currentDigit = digit
            currentCount = 1
        }
    }

    push()
}