package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day21() = puzzle(day = 21) {
    fun solve(program: String) = with(
        startComputer(input, program.lines().flatMap { it.toList().map { c -> c.code.toLong() } + 10L })
    ) {
        runUntilHalt()
        output.last { it > 0xFF }.s()
    }

    partOne = solve("""
        NOT C T
        NOT B J
        AND D T
        NOT A J
        OR T J
        WALK
    """.trimIndent())

    partTwo = solve("""
        NOT C J
        NOT B T
        OR T J
        AND H J
        AND D J
        NOT A T
        OR T J
        RUN
    """.trimIndent())
}