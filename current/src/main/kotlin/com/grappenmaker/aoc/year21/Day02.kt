package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day02() = puzzle(day = 2) {
    var depth = 0
    var x = 0
    var aim = 0

    for (line in inputLines) {
        val (c, n) = line.split(' ')
        val d = n.toInt()

        when (c) {
            "down" -> aim += d
            "up" -> aim -= d
            "forward" -> {
                x += d
                depth += d * aim
            }
        }
    }

    partOne = aim * x
    partTwo = depth * x
}