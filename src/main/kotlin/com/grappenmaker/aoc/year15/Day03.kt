package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.Direction
import com.grappenmaker.aoc.Point
import com.grappenmaker.aoc.plus
import com.grappenmaker.aoc.toPoint

fun PuzzleSet.day3() = puzzle {
    val directions = input.map { it.toDirection().toPoint() }
    fun createPath(startIdx: Int, jump: Int) =
        directions.drop(startIdx).chunked(jump).map { (a) -> a }.scan(Point(0, 0)) { a, d -> a + d }

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