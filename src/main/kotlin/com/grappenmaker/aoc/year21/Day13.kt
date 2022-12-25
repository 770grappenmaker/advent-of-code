package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day13() = puzzle(day = 13) {
    val parts = input.split("\n\n")
    val points = parts.first().split("\n").map { it.split(",").map(String::toInt).asPair().toPoint() }
    val folds = parts[1].split("\n").map {
        val fold = it.substring(11).split("=")
        enumValueOf<Direction>(fold.first().uppercase()) to fold[1].toInt()
    }

    // Part one
    val doFold = { pts: List<Point>, (dir, loc): Pair<Direction, Int> ->
        pts.map { point ->
            val (x, y) = point
            when {
                dir == Direction.X && x > loc -> Point(loc - (x - loc), y)
                dir == Direction.Y && y > loc -> Point(x, loc - (y - loc))
                else -> point
            }
        }
    }

    // Part two
    val allFolded = folds.fold(points) { cur, fold -> doFold(cur, fold) }.distinct()

    partOne = doFold(points, folds.first()).distinct().size.s()
    partTwo = "\n" + allFolded.asBooleanGrid().debug(on = "#", off = " ")
}

enum class Direction {
    X, Y
}