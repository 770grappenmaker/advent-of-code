package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.repeatInfinitely
import com.grappenmaker.aoc.splitInts
import kotlin.math.max

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(day = 21) {
    val (startOne, startTwo) = inputLines.map { it.splitInts().last() }
    data class PlayerState(
        val pos: Int,
        val score: Int = 0,
    ) {
        fun advance(rolls: Int): PlayerState {
            val new = (pos + rolls - 1) % 10 + 1
            return PlayerState(new, score + new)
        }
    }

    data class GameState(val p1: PlayerState, val p2: PlayerState, val turn: Boolean = true) {
        fun advance(rolls: Int) = GameState(
            p1 = if (turn) p1.advance(rolls) else p1,
            p2 = if (!turn) p2.advance(rolls) else p2,
            turn = !turn
        )

        fun winner(score: Int): PlayerState? = p1.takeIf { it.score >= score } ?: p2.takeIf { it.score >= score }
        fun other(player: PlayerState) = if (p1 == player) p2 else p1
    }

    var rolls = 0
    val die = (1..100).asSequence().repeatInfinitely().onEach { rolls++ }.iterator()
    val initial = GameState(PlayerState(startOne), PlayerState(startTwo))
    var state = initial

    while (true) {
        state = state.advance(die.next() + die.next() + die.next())
        val winner = state.winner(1000)

        if (winner != null) {
            partOne = (state.other(winner).score * rolls).s()
            break
        }
    }

    data class Stats(val p1: Long, val p2: Long)
    operator fun Stats.plus(other: Stats) = Stats(p1 + other.p1, p2 + other.p2)
    operator fun Stats.times(other: Long) = Stats(p1 * other, p2 * other)
    fun Iterable<Stats>.sum() = reduce { acc, curr -> acc + curr }

    val outcomes = (1..3).toList()
    val poss = outcomes.flatMap { a -> outcomes.flatMap { b -> outcomes.map { c -> listOf(a, b, c).sum() } } }
    val freq = poss.groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }

    val memo = hashMapOf<GameState, Stats>()
    fun GameState.find(): Stats = memo.getOrPut(this) {
        when (val winner = winner(21)) {
            null -> freq.map { (roll, f) -> advance(roll).find() * f }.sum()
            else -> Stats(
                p1 = if (winner == p1) 1 else 0,
                p2 = if (winner == p2) 1 else 0
            )
        }
    }

    val total = initial.find()
    partTwo = max(total.p1, total.p2).s()
}