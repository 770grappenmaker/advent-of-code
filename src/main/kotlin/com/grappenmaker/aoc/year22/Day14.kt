package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.BlockType.*
import com.grappenmaker.aoc.year22.Direction.*

fun PuzzleSet.day14() = puzzle {
    val lines = inputLines.flatMap { l ->
        l.split(" -> ").map { it.split(",").map(String::toInt).asPair().toPoint() }
            .windowed(2).map { (a, b) -> a..b }.connect()
    }

    val initial = lines.asBooleanGrid().mapElements { if (it) WALL else EMPTY }
    val sandLocation = Point(500, 0)
    val directions = listOf(DOWN.toPoint(), DOWN + LEFT, DOWN + RIGHT)

    fun MutableGrid<BlockType>.sim(): Int {
        outer@ while (true) {
            var curr = sandLocation
            while (true) {
                val poss = directions.map { curr + it }.filter { it in this }
                if (poss.isEmpty()) break@outer

                val new = poss.firstOrNull { this[it] == EMPTY }
                if (new != null) {
                    curr = new
                    continue
                }

                if (this[curr] == SAND) break@outer
                this[curr] = SAND
                break
            }
        }

        return count { it == SAND }
    }

    partOne = initial.asMutableGrid().sim().s()

    val newHeight = initial.height + 2
    partTwo = initial.extend(width = initial.width * 2, height = newHeight)
    { if (it.y == newHeight - 1) WALL else EMPTY }.asMutableGrid().sim().s()
}

enum class BlockType {
    EMPTY, SAND, WALL;

    override fun toString() = when (this) {
        EMPTY -> "."
        SAND -> "o"
        WALL -> "#"
    }
}