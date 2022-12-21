package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.*
import com.grappenmaker.aoc.year22.Point

fun PuzzleSet.day20() = puzzle(20) {
    val (algo, input) = input.split("\n\n")
    val lookup = algo.map { it == '#' }
    val image = input.lines().asGrid { it == '#' }

    val adj = listOf(
        Point(-1, -1), Point(0, -1), Point(1, -1),
        Point(-1, 0), Point(0, 0), Point(1, 0),
        Point(-1, 1), Point(0, 1), Point(1, 1)
    )

    fun stepN(n: Int): String {
        fun Grid<Boolean>.step(default: Boolean): Grid<Boolean> {
            val new = expandEmpty()
            return new.mapIndexedElements { loc, _ ->
                lookup[
                    adj.map { if ((new.getOrNull(loc + it) == true) == default) 1 else 0 }.asReversed()
                        .reduceIndexed { idx, acc, curr -> acc or (curr shl idx) }
                ] != default
            }
        }

        var curr = image
        repeat(n) { curr = curr.step(it % 2 == 0) }
        return curr.countTrue().s()
    }

    partOne = stepN(2)
    partTwo = stepN(50)
}