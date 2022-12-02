package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.RPS.*

// Today's code was very naughty
fun PuzzleSet.day2() = puzzle {
    val inputs = inputLines.map {
        val (one, two) = it.split(" ").map { c ->
            when (c) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> error("Invalid input")
            }
        }

        Play(one, two)
    }

    partOne = inputs.score.s()
    partTwo = inputs.map { Play(it.shouldPlay, it.right) }.score.s()
}

val List<Play>.score get() = sumOf { it.totalScore }

data class Play(val left: RPS, val right: RPS)

val Play.totalScore get() = score + win
val Play.score get() = left.score
val Play.win
    get() = when {
        right.wins == left -> 6
        left == right -> 3
        right.loses == left -> 0
        else -> error("Impossible")
    }

// Part two
val Play.shouldPlay
    get() = when (right) {
        ROCK -> left.wins
        PAPER -> left
        SCISSORS -> left.loses
    }

enum class RPS(
    val score: Int,
    private val winsFromIdx: Int,
    private val losesAgainstIdx: Int
) {
    ROCK(1, 2, 1), PAPER(2, 0, 2), SCISSORS(3, 1, 0);

    val wins get() = values()[winsFromIdx]
    val loses get() = values()[losesAgainstIdx]
}