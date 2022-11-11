package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day3() = puzzle {
    val directions = input.map { it.toDirection() }
    val createPath = { startIdx: Int, jump: Int ->
        generateSequence(House(0, 0) to startIdx) { (h, idx) -> 
            directions.getOrNull(idx)?.let { dir ->
                House(h.x + dir.dx, h.y + dir.dy) to idx + jump
            }
        }.map { (h) -> h }.toList()
    }

    partOne = createPath(0, 1).toSet().size.s()
    
    val santaPath = createPath(0, 2)
    val roboSantaPath = createPath(1, 2)
    partTwo = (santaPath + roboSantaPath).toSet().size.s()
}

data class House(val x: Int, val y: Int)

enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, 1), SOUTH(0, -1), EAST(1, 0), WEST(-1, 0)
}

fun Char.toDirection() = when(this) {
    '^' -> Direction.NORTH
    'v' -> Direction.SOUTH
    '>' -> Direction.EAST
    '<' -> Direction.WEST
    else -> error("Impossible")
}