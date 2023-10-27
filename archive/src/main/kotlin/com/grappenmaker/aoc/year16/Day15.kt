package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allIdentical
import com.grappenmaker.aoc.product
import com.grappenmaker.aoc.splitInts

fun PuzzleSet.day15() = puzzle(day = 15) {
    data class Disc(val positions: Int, val current: Int)

    val initialDiscs = inputLines.map {
        val (_, pos, _, curr) = it.splitInts()
        Disc(pos, curr)
    }

    fun solve(discs: List<Disc>): String {
        val totalPeriod = discs.map { it.positions }.product()
        val totalPoss = generateSequence(discs) { d ->
            d.map { it.copy(current = (it.current + 1) % it.positions) }
        }.take(totalPeriod).toList()

        return ((0..totalPeriod - discs.size).first { t ->
            (t..<t + discs.size).map { totalPoss[it][it - t].current }.allIdentical()
        } - 1).s()
    }

    partOne = solve(initialDiscs)
    partTwo = solve(initialDiscs + Disc(11, 0))
}