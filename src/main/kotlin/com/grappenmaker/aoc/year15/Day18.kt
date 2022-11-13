package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

val adjacentStraight = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
val adjacentDiagonal = listOf(-1 to -1, 1 to 1, -1 to 1, 1 to -1)
val allAdjacent = adjacentStraight + adjacentDiagonal

fun Light.adjacents(width: Int, height: Int) = allAdjacent.asSequence()
    .map { (dx, dy) -> copy(x = x + dx, y = y + dy) }
    .filter { (x, y) -> x in 0 until width && y in 0 until height }

fun Light.asIndex(width: Int) = x + y * width
fun Int.asLight(width: Int) = Light(this % width, this / width)

fun PuzzleSet.day18() = puzzle {
    // true = on, false = off
    val state = inputLines.flatMap { l -> l.trim().map { it == '#' } }
    val width = inputLines.first().trim().length
    val corners = hashSetOf(
        Light(0, 0), // tl
        Light(0, width - 1), // bl
        Light(width - 1, 0), // tr
        Light(width - 1, width - 1) // br
    )

    fun solve(partTwo: Boolean): String = generateSequence(state) { currentState ->
        currentState.mapIndexed { idx, on ->
            val asLight = idx.asLight(width)
            val onNeighbours = asLight.adjacents(width, width)
                .count { (partTwo && it in corners) || currentState[it.asIndex(width)] }

            when {
                partTwo && asLight in corners -> true
                on && onNeighbours in 2..3 -> true
                !on && onNeighbours == 3 -> true
                else -> false
            }
        }
    }.drop(1).take(100).last().count { it }.s()

    partOne = solve(false)
    partTwo = solve(true)
}