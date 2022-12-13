package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year22.Direction.*
import com.grappenmaker.aoc.year22.Point
import com.grappenmaker.aoc.year22.manhattanDistance
import com.grappenmaker.aoc.year22.plus

fun main() = simplePuzzle(11, 2017) {
    val dirs = input.split(",").map {
        when (it) {
            "n" -> UP + UP
            "s" -> DOWN + DOWN
            "nw" -> UP + LEFT
            "ne" -> UP + RIGHT
            "sw" -> DOWN + LEFT
            "se" -> DOWN + RIGHT
            else -> error("Invalid direction $it")
        }
    }

    val route = dirs.scan(Point(0, 0)) { acc, curr -> acc + curr }
    partOne = (route.last().manhattanDistance / 2).s()
    partTwo = (route.maxOf { it.manhattanDistance } / 2).s()
}