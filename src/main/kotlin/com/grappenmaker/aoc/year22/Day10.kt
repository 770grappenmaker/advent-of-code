package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day10() = puzzle {
    val program = sequence {
        var curr = 1
        inputLines.forEach { insn ->
            val split = insn.split(" ")
            when (split[0]) {
                "noop" -> yield(curr)
                "addx" -> {
                    yield(curr)
                    yield((curr + split[1].toInt()).also { curr = it })
                }
            }
        }
    }.toList()

    fun signalStrength(cycle: Int) = program[cycle - 2] * cycle
    partOne = listOf(20, 60, 100, 140, 180, 220).sumOf { signalStrength(it) }.s()

    partTwo = "\n" + buildBooleanGrid(40, 6) {
        var spritePos = 1
        val sprite = { y: Int -> Rectangle(Point(spritePos - 1, y), Point(spritePos + 1, y)) }

        program.forEachIndexed { idx, x ->
            val point = pointFromIndex(idx)
            if (point in sprite(point.y)) enable(point)

            spritePos = x
        }
    }.debug(on = "oo ", off = "   ")
}