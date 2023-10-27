package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.doubleLines
import com.grappenmaker.aoc.toQueue

fun PuzzleSet.day22() = puzzle(day = 22) {
    val (initialP1, initialP2) = input.doubleLines().map { it.lines().drop(1).map(String::toInt) }

    data class Outcome(val p1: List<Int> = initialP1, val p2: List<Int> = initialP2)
    fun Outcome.score() = (p1.takeIf { it.isNotEmpty() } ?: p2)
        .asReversed().foldIndexed(0L) { idx, acc, v -> acc + (idx + 1) * v }.s()

    fun Outcome.playerOneWins() = p1.isNotEmpty()

    fun step(
        startP1: List<Int> = initialP1,
        startP2: List<Int> = initialP2,
        partTwo: Boolean = false,
        update: (p1: MutableList<Int>, p2: MutableList<Int>) -> Unit
    ): Outcome {
        val p1 = startP1.toQueue()
        val p2 = startP2.toQueue()
        val seen = hashSetOf((p1 to p2).hashCode())

        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            update(p1, p2)
            if (partTwo && !seen.add((p1 to p2).hashCode())) break
        }

        return Outcome(p1, p2)
    }

    partOne = step { p1, p2 ->
        val a = p1.removeFirst()
        val b = p2.removeFirst()
        val target = if (a > b) p1 else p2
        target += maxOf(a, b)
        target += minOf(a, b)
    }.score()

    fun recurse(sp1: List<Int> = initialP1, sp2: List<Int> = initialP2): Outcome = step(sp1, sp2, true) { p1, p2 ->
        val a = p1.removeFirst()
        val b = p2.removeFirst()

        val p1Wins = when {
            p1.size < a || p2.size < b -> a > b
            else -> recurse(p1.take(a), p2.take(b)).playerOneWins()
        }

        val winner = if (p1Wins) p1 else p2
        winner += if (p1Wins) a else b
        winner += if (p1Wins) b else a
    }

    partTwo = recurse().score()
}