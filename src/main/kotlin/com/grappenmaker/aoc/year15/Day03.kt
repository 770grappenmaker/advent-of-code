package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction
import com.grappenmaker.aoc.year22.Point
import com.grappenmaker.aoc.year22.plus

fun PuzzleSet.day3() = puzzle {
    val directions = input.map { it.toDirection() }
    val createPath = { startIdx: Int, jump: Int ->
        generateSequence(Point(0, 0) to startIdx) { (curr, idx) ->
            directions.getOrNull(idx)?.let { curr + it to idx + jump }
        }.map { (h) -> h }.toList()
    }

    partOne = createPath(0, 1).toSet().size.s()

    val santaPath = createPath(0, 2)
    val roboSantaPath = createPath(1, 2)
    partTwo = (santaPath + roboSantaPath).toSet().size.s()
}

fun Char.toDirection() = when (this) {
    '^' -> Direction.UP
    'v' -> Direction.DOWN
    '>' -> Direction.RIGHT
    '<' -> Direction.LEFT
    else -> error("Impossible")
}