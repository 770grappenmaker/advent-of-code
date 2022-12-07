package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year21.buildRepeated
import com.grappenmaker.aoc.year21.generateSequenceIndexed
import com.grappenmaker.aoc.year22.*

// Thought I could practice a bit...
// Turns out I spent hours debugging, to end up finding out...
// I read the puzzle wrong...
fun PuzzleSet.day1() = puzzle {
    val steps = input.split(", ").map { (if (it[0] == 'L') -1 else 1) to it.substring(1).toInt() }
    val path = generateSequenceIndexed(listOf(Point(0, 0)) to Direction.UP) { idx, (l, dir) ->
        steps.getOrNull(idx)?.let { (turn, amount) ->
            val newDir = dir.next(turn)
            val curr = l.last()
            buildRepeated(amount) { curr + (newDir * (it + 1)) } to newDir
        }
    }.flatMap { (a) -> a }.toList()

    partOne = path.last().manhattanDistance.s()
    partTwo = path.firstNotDistinct().manhattanDistance.s()
}