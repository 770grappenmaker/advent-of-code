package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*

fun PuzzleSet.day11() = puzzle(11) {
    val initial = inputLines.asCharGrid()

    fun solve(partTwo: Boolean, dist: Int) = initial.automaton { p, s ->
        val adj = if (partTwo) {
            dAllAdjacent.mapNotNull { dir ->
                generateSequence(p) { it + dir }.drop(1).takeWhile { it in this }
                    .map { this[it] }.firstOrNull { it != '.' }
            }
        } else p.allAdjacentElements()

        when {
            s == 'L' && '#' !in adj -> '#'
            s == '#' && adj.countContains('#') >= dist -> 'L'
            else -> s
        }
    }.firstNotDistinctBy { it.elements }.countContains('#').s()

    partOne = solve(false, 4)
    partTwo = solve(true, 5)
}