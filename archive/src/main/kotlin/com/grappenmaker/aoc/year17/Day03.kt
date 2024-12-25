package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.ceil
import kotlin.math.sqrt

@PuzzleEntry
fun PuzzleSet.day3() = puzzle {
    // Welcome to the yearly competition of "what in the jank"!
    val inputSquare = input.toInt()
    val ringWidth = ceil(sqrt(inputSquare.toDouble())).toInt()
    assert(ringWidth % 2 != 0)

    val grid = mutableGrid(ringWidth, ringWidth) { 0 }
    val center = Point(ringWidth / 2, ringWidth / 2)
    var curr = center
    grid[curr] = 1

    var currRing = Rectangle(curr, curr)
    val one = Point(1, 1)
    var direction = Direction.RIGHT
    var steps = 0

    fun step(calculate: Boolean) {
        val possibleNext = curr + direction
        val nextDir = direction.next(-1)
        curr = when (possibleNext) {
            !in currRing -> when (direction) {
                Direction.RIGHT -> {
                    currRing = Rectangle(currRing.a - one, currRing.b + one)
                    possibleNext
                }

                else -> curr + nextDir
            }.also { direction = nextDir }

            else -> possibleNext
        }

        if (calculate) with (grid) { this[curr] = curr.allAdjacent().sumOf { this[it] } }
        steps++
    }

    while (grid[curr] < inputSquare) {
        step(true)
    }

    partTwo = grid[curr].toString()

    repeat(inputSquare - steps - 1) { step(false) }
    partOne = (curr manhattanDistanceTo center).toString()
}