package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day5() = puzzle {
    val (initial, guide) = rawInput.split("\n\n")
    val initialState = initial.lines().dropLast(1).asReversed().map { it.toList() }.swapOrder()
        .chunked(4) { p -> p[1].takeWhile { it.isUpperCase() } }

    val moves = guide.lines().map { l -> l.split(" ").mapNotNull { it.toIntOrNull() } }

    fun solve(partTwo: Boolean): String {
        val cloned = initialState.map { it.toMutableList() }

        moves.forEach { (amount, from, to) ->
            val toAdd = cloned[from - 1].removeLastN(amount)
            cloned[to - 1].addAll(if (!partTwo) toAdd.asReversed() else toAdd)
        }

        return cloned.joinToString("") { it.last().toString() }
    }

    partOne = solve(false)
    partTwo = solve(true)
}