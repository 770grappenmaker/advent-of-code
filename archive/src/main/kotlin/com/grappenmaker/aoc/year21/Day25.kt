package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction
import com.grappenmaker.aoc.Direction.DOWN
import com.grappenmaker.aoc.Direction.RIGHT
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    fun GridLike<Char>.pass(dir: Direction, type: Char) = with(this) {
        asMutableGrid().also { pass ->
            points.forEach { p ->
                val next = (p + dir).mapX { it % width }.mapY { it % height }
                if (this[p] == type && this[next] == '.') {
                    pass[p] = '.'
                    pass[next] = type
                }
            }
        }
    }

    partOne = (generateSequence<GridLike<Char>>(inputLines.asCharGrid()) { it.pass(RIGHT, '>').pass(DOWN, 'v') }
        .zipWithNext().indexOfFirst { (a, b) -> a == b } + 1).toString()
}