package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(day = 19) {
    val grid = inputLines.asCharGrid()
    val start = grid.row(0).first { grid[it] == '|' }

    with (grid) {
        var steps = 0
        val seenLetters = mutableListOf<Char>()
        var position = start
        var direction = Direction.DOWN

        while (position in this) {
            val curr = this[position]
            if (curr in 'A'..'Z') seenLetters += curr
            if (curr == '+') {
                val newPosition = (position.adjacentSides() - (position - direction))
                    .singleOrNull { this[it] != ' ' } ?: break

                direction = (newPosition - position).asDirection()
                position = newPosition
            } else position += direction

            steps++
        }

        partOne = seenLetters.joinToString("")
        partTwo = steps.s()
    }
}