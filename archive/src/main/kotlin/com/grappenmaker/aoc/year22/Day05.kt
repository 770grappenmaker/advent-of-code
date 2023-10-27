package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.removeLastN
import com.grappenmaker.aoc.swapOrder
import com.grappenmaker.aoc.toQueue

fun PuzzleSet.day5() = puzzle {
    val (initial, guide) = rawInput.split("\n\n")
    val stackLines = initial.lines().dropLast(1)
    val initialState = stackLines.map { it.slice(1..<it.length step 4).toList() }
        .swapOrder().map { it.dropWhile { c -> c == ' ' }.asReversed() }

    val moves = guide.lines().map { l -> l.split(" ").mapNotNull { it.toIntOrNull() } }

    fun solve(partTwo: Boolean): String {
        val cloned = initialState.map { it.toQueue() }

        moves.forEach { (amount, from, to) ->
            val toAdd = cloned[from - 1].removeLastN(amount)
            cloned[to - 1].addAll(if (!partTwo) toAdd.asReversed() else toAdd)
        }

        return cloned.joinToString("") { it.last().toString() }
    }

    partOne = solve(false)
    partTwo = solve(true)
}