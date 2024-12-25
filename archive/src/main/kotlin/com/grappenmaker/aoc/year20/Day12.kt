package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(12) {
    val actions = inputLines.map { l -> l.first() to l.drop(1).toInt() }
    partOne = actions.fold(Point(0, 0) to RIGHT) { (p, d), (a, v) ->
        when (a) {
            'N' -> (p + UP * v) to d
            'E' -> (p + RIGHT * v) to d
            'S' -> (p + DOWN * v) to d
            'W' -> (p + LEFT * v) to d
            'L' -> p to d.next(-v / 90)
            'R' -> p to d.next(v / 90)
            'F' -> (p + d * v) to d
            else -> error("Invalid action $a")
        }
    }.first.manhattanDistance.toString()

    partTwo = actions.fold(Point(0, 0) to Point(10, -1)) { (s, w), (a, v) ->
        when (a) {
            'N' -> s to (w + UP * v)
            'E' -> s to (w + RIGHT * v)
            'S' -> s to (w + DOWN * v)
            'W' -> s to (w + LEFT * v)
            'L', 'R' -> s to w.applyN(if (a == 'L') 4 - v / 90 else v / 90) { Point(-it.y, it.x) }
            'F' -> (s + w * v) to w
            else -> error("Invalid action $a")
        }
    }.first.manhattanDistance.toString()
}