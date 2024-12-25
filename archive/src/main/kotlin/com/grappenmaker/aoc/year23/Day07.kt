package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(day = 7) {
    data class Hand(val l: List<Char>, val bid: Long)
    val hs = inputLines.map { l ->
        val (a, b) = l.split(" ")
        Hand(a.toList(), b.toLong())
    }

    fun solve(partTwo: Boolean, order: String): String {
        val o = order.split(", ").map { it.single() }
        val jo = o.filter { it != 'J' }

        fun List<Char>.type(): Int {
            val count = frequencies().values.frequencies()

            return when {
                5 in count -> 0
                4 in count -> 1
                3 in count && 2 in count -> 2
                3 in count -> 3
                (count[2] ?: 0) == 2 -> 4
                2 in count -> 5
                else -> 6
            }
        }

        fun List<Char>.optimizeJokers() = jo.map { a -> map { if (it == 'J') a else it } }.minBy { it.type() }

        val res = hs.sortedWith(compareByDescending<Hand> { h ->
            h.l.let { if (partTwo) it.optimizeJokers() else it }.type()
        }.thenComparator { a, b ->
            a.l.indices.firstNotNullOf { i -> (o.indexOf(b.l[i]) - o.indexOf(a.l[i])).takeIf { it != 0 } }
        })

        return res.mapIndexed { idx, h -> (h.bid * (idx + 1)) }.sum().toString()
    }

    partOne = solve(false, "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2")
    partTwo = solve(true, "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J")
}