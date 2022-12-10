package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.abs

fun PuzzleSet.day10() = puzzle {
    val cycles = buildList {
        var curr = 1
        inputLines.forEach { insn ->
            val split = insn.split(" ")
            when (split[0]) {
                "noop" -> add(curr)
                "addx" -> {
                    add(curr)
                    add((curr + split[1].toInt()).also { curr = it })
                }
            }
        }
    }

    partOne = (20..220 step 40).sumOf { cycles[it - 2] * it }.s()
    partTwo = "\n" + buildBooleanGrid(40, 6) {
        var spritePos = 1
        cycles.forEachIndexed { idx, x ->
            val point = pointFromIndex(idx)
            if (abs(spritePos - point.x) <= 1) enable(point)
            spritePos = x
        }
    }.debug(on = "⬜", off = "⬛")
}