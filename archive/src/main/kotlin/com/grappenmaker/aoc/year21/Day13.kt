package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day13() = puzzle(day = 13) {
    val (pointsPart, foldsPart) = input.split("\n\n")
    val points = pointsPart.lines().mapTo(hashSetOf()) { it.split(",").map(String::toInt).asPair().toPoint() }
    val folds = foldsPart.lines().map {
        val (d, v) = it.substring(11).split("=")
        enumValueOf<Direction>(d.uppercase()) to v.toInt()
    }

    // Part one
    val doFold = { pts: Set<Point>, (dir, loc): Pair<Direction, Int> ->
        pts.mapTo(hashSetOf()) { point ->
            val (x, y) = point
            when {
                dir == Direction.X && x > loc -> Point(loc - (x - loc), y)
                dir == Direction.Y && y > loc -> Point(x, loc - (y - loc))
                else -> point
            }
        }
    }

    // Part two
    val allFolded = folds.fold(points) { cur, fold -> doFold(cur, fold) }
    partOne = doFold(points, folds.first()).size.s()
    partTwo = "\n" + allFolded.asBooleanGrid().debug(off = " ")
}

enum class Direction {
    X, Y
}