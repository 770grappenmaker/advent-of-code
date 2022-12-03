package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.RPS.*

// Today's code was very naughty
fun PuzzleSet.day2() = puzzle {
    val inputs = inputLines.map { Guide(rpsFrom(it[0] - 'A'), rpsFrom(it[2] - 'X')) }
    partOne = inputs.score.s()
    partTwo = inputs.map { Guide(it.left, it.shouldPlay) }.score.s()
}

val List<Guide>.score get() = sumOf { it.totalScore }

data class Guide(val left: RPS, val right: RPS)

val Guide.totalScore get() = score + win
val Guide.score get() = right.score
val Guide.win
    get() = when {
        right.wins == left -> 6
        left == right -> 3
        right.loses == left -> 0
        else -> error("Impossible")
    }

// Part two
val Guide.shouldPlay
    get() = when (right) {
        ROCK -> left.wins
        PAPER -> left
        SCISSORS -> left.loses
    }

// Why did I think this was a good idea?
enum class RPS(
    val score: Int,
    private val winsFromIdx: Int,
    private val losesAgainstIdx: Int
) {
    ROCK(1, 2, 1), PAPER(2, 0, 2), SCISSORS(3, 1, 0);

    val wins get() = values()[winsFromIdx]
    val loses get() = values()[losesAgainstIdx]
}

fun rpsFrom(idx: Int) = enumValues<RPS>()[idx]