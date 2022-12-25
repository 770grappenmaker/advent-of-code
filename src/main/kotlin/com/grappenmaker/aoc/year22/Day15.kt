package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day15() = puzzle {
    val scanners = inputLines.map { l ->
        val ints = l.splitInts()
        Scanner(Point(ints[0], ints[1]), Point(ints[2], ints[3]))
    }

    fun Point.isValid() = scanners.none { it.loc manhattanDistanceTo this <= it.distance }

    val min = scanners.minOf { it.loc.x - it.distance }
    val max = scanners.maxOf { it.loc.x + it.distance }
    partOne = (min..max).count { !Point(it, 2000000).isValid() }.s()

    val range = 0..4000000
    val directions = listOf(DOWN + LEFT, DOWN + RIGHT, UP + LEFT, UP + RIGHT)
    val (x, y) = scanners.asSequence().flatMap { scan ->
        val away = scan.distance + 1
        (0..away).flatMap { dx ->
            val dy = away - dx
            directions.map { (Point(dx, dy) * it) + scan.loc }
        }.filter { (x, y) -> x in range && y in range }
    }.first { it.isValid() }

    partTwo = (x.toLong() * 4000000 + y.toLong()).s()
}

data class Scanner(val loc: Point, val beacon: Point) {
    val distance = loc manhattanDistanceTo beacon
}