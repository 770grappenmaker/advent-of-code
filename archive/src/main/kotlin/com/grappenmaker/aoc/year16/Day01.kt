package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*

// Thought I could practice a bit...
// Turns out I spent hours debugging, to end up finding out...
// I read the puzzle wrong...
fun PuzzleSet.day1() = puzzle {
    val steps = input.split(", ").map { (if (it[0] == 'L') -1 else 1) to it.substring(1).toInt() }
    val path = steps.scan(listOf(Point(0, 0)) to Direction.UP) { (l, dir), (turn, amount) ->
        val newDir = dir.next(turn)
        List(amount) { l.last() + (newDir * (it + 1)) } to newDir
    }.flatMap { (a) -> a }

    partOne = path.last().manhattanDistance.s()
    partTwo = path.firstNotDistinct().manhattanDistance.s()
}