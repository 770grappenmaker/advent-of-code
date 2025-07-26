package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day02() = puzzle(day = 2) {
    partOne = inputLines.sumOf { l ->
        val nums = l.ints().sorted()
        nums.last() - nums.first()
    }

    partTwo = inputLines.sumOf { l ->
        val nums = l.ints()
        val (a, b) = nums.permPairsExclusive().single { (a, b) -> a % b == 0 }
        a / b
    }
}