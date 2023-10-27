package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day24() = puzzle(day = 24) {
    fun Char.parseDirection() = when (this) {
        'n' -> UP
        'e' -> RIGHT
        's' -> DOWN
        'w' -> LEFT
        else -> error("Invalid direction $this")
    }

    fun String.parse(): Point {
        var idx = 0
        var result = Point(0, 0)

        while (idx in indices) {
            val curr = this[idx]
            val parsed = curr.parseDirection()
            result += when (curr) {
                'n', 's' -> when(getOrNull(idx + 1)) {
                    'e' -> (parsed + RIGHT).also { idx++ }
                    'w' -> (parsed + LEFT).also { idx++ }
                    else -> parsed.let { it + it }
                }
                else -> parsed.let { it + it }
            }

            idx++
        }

        return result
    }

    val instructions = inputLines.map { l -> l.parse() }
    val initialToggled = hashSetOf<Point>()

    instructions.forEach { p -> if (p in initialToggled) initialToggled -= p else initialToggled += p }
    partOne = initialToggled.size.s()

    // hacky code but works :D
    val dHex = listOf(
        LEFT + LEFT,
        RIGHT + RIGHT,
        UP + LEFT,
        UP + RIGHT,
        DOWN + LEFT,
        DOWN + RIGHT,
    )

    fun Point.neighHex() = dHex.map { it + this }

    val d = Point(1, 1)
    partTwo = generateSequence(initialToggled.toSet()) { toggled ->
        val min = toggled.minBound()
        val max = toggled.maxBound()

        Rectangle(min - d, max + d).points.filterTo(hashSetOf()) { t ->
            val neighs = t.neighHex().count { it in toggled }
            if (t in toggled) neighs != 0 && neighs <= 2 else neighs == 2
        }
    }.nth(100).size.s()
}