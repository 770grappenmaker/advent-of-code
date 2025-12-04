package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(4, 2025) {
    val g = inputLines.asGrid { it == '@' }.asMutableGrid()
    fun MutableBooleanGrid.iter() = pointsSequence.filter { this[it] && it.allAdjacent().count { p -> this[p] } < 4 }

    partOne = g.iter().count()
    partTwo = generateSequence { g.iter().onEach { g[it] = false }.count().takeIf { it != 0 } }.sum()
}