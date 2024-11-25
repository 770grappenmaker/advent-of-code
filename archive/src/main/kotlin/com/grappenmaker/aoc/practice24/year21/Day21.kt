@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day21() = puzzle(day = 21) {
    data class Player(val pos: Int, val score: Int = 0)
    data class State(val p1: Player, val p2: Player, val turn: Boolean = false)
    fun Player.roll(sum: Int): Player {
        val np = (pos + sum) % 10
        val ns = score + np + 1

        return Player(np, ns)
    }

    val perm = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
    val memo = hashMapOf<State, Pair<Long, Long>>()

    fun solve(s: State): Pair<Long, Long> = memo.getOrPut(s) {
        val (p1, p2, turn) = s
        if (p1.score >= 21) return 1L to 0L
        if (p2.score >= 21) return 0L to 1L

        var a = 0L
        var b = 0L

        for ((p, t) in perm) {
            val (na, nb) = solve(State(
                if (turn) p1 else p1.roll(p),
                if (turn) p2.roll(p) else p2,
                !turn
            ))

            a += na * t
            b += nb * t
        }

        return a to b
    }

    val (p1, p2) = inputLines.map { it.last().digitToInt() - 1 }
    val (a, b) = solve(State(Player(p1), Player(p2)))
    partTwo = maxOf(a, b)
}