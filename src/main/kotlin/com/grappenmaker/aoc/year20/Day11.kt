package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.year20.SeatState.*

fun PuzzleSet.day11() = puzzle(11) {
    val initial = inputLines.asGrid {
        when (it) {
            '#' -> OCCUPIED
            '.' -> NO_SEAT
            'L' -> EMPTY
            else -> error("Should not happen")
        }
    }

    fun Grid<SeatState>.step(partTwo: Boolean, dist: Int) = mapIndexedElements { p, s ->
        val adj = if (partTwo) {
            dAllAdjacent.mapNotNull { dir ->
                generateSequence(p) { it + dir }.drop(1).takeWhile { it in this }
                    .map { this[it] }.firstOrNull { it != NO_SEAT }
            }
        } else p.allAdjacentElements()

        when {
            s == EMPTY && OCCUPIED !in adj -> OCCUPIED
            s == OCCUPIED && adj.countContains(OCCUPIED) >= dist -> EMPTY
            else -> s
        }
    }

    fun solve(partTwo: Boolean, dist: Int) = generateSequence(initial) { it.step(partTwo, dist) }
        .firstNotDistinctBy { it.elements }.count { it == OCCUPIED }.s()

    partOne = solve(false, 4)
    partTwo = solve(true, 5)
}

enum class SeatState {
    OCCUPIED, EMPTY, NO_SEAT
}