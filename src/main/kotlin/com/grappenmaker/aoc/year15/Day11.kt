package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.abs

fun PuzzleSet.day11() = puzzle {
    val password = input.trim().map { it.digitCode }
    val (first, second) = generateSequence(password) { it.nextPassword() }
        .filter { it.isValid() }.map { pwd -> pwd.joinToString("") { it.digitChar.toString() } }
        .take(2).toList()

    partOne = first
    partTwo = second
}

// Unlike incrementPassword, this one also skips if one of the illegal values is found
fun List<Int>.nextPassword(max: Int = 26) = when (val pivot = indexOfFirst { it in passwordDisallowed }) {
    -1 -> incrementPassword(max)
    else -> {
        // 1. Cut off until the pivot
        // 2. Add pivot + 1
        // 3. Add zeroes
        take(pivot) + (this[pivot] + 1) + (1 until size - pivot).map { 0 }
    }
}

val passwordDisallowed = listOf('i', 'o', 'l').map { it.digitCode }

fun List<Int>.incrementPassword(max: Int = 26) = if (isEmpty()) emptyList() else buildList {
    fun increment(index: Int) {
        // Calculate incremented value
        val newValue = (this@incrementPassword[index] + 1) % max

        // Add to top of list
        add(0, newValue)

        // If value wrapped, increment next index as well
        if (newValue == 0) increment(index - 1)
        // Else, finish and add the rest
        else addAll(0, this@incrementPassword.take(index))
    }

    increment(this@incrementPassword.indices.last)
}

val Char.digitCode get() = code - 97
val Int.digitChar get() = (this + 97).toChar()

fun List<Int>.isValid() =
    asSequence().windowed(3).any { (a, b, c) -> b - a == 1 && c - b == 1 } &&
            windowed(2).withIndex().filter { (_, p) -> p[0] == p[1] }.let { pairs ->
                pairs.any { (idxA) -> pairs.any { (idxB) -> abs(idxA - idxB) >= 2 } }
            }