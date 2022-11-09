package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day13() = puzzle(day = 13) {
    val parts = input.split("\n\n")
    val points = parts.first().split("\n").map { line ->
        val nums = line.split(",")
        Point(nums[0].toInt(), nums[1].toInt())
    }

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
    val width = allFolded.maxOf { it.x }
    val height = allFolded.maxOf { it.y }

    val sep = System.lineSeparator()
    val partTwoFolds = (0..height).joinToString(sep) { col ->
        (0..width).joinToString(" ") { row ->
            if (allFolded.find { it.x == row && it.y == col } != null) "#" else " "
        }
    }

    partOne = doFold(points, folds.first()).distinct().size.s()
    partTwo = (sep + partTwoFolds)
}

enum class Direction {
    X, Y
}