package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day3() = puzzle {
    with(inputLines.asGrid { it == '#' }) {
        val countTrees = { slope: Point ->
            generateSequence(topLeftCorner) { it + slope }
                .map { p -> p.mapX { it % width } }
                .takeWhile { it in this }
                .sumOf { if (this[it]) 1.toInt() else 0 }
        }

        partOne = countTrees(Point(3, 1)).s()
        partTwo = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { countTrees(it.toPoint()) }.product().s()
    }
}