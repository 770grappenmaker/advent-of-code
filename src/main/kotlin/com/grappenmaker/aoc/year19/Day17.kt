package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.asCharGrid
import com.grappenmaker.aoc.year22.points

fun PuzzleSet.day17() = puzzle(17) {
    val prog = input.split(",").map(String::toLong)
    val charGrid = prog.programResults()
        .joinToString("") { it.toInt().toChar().toString() }.trim().lines().asCharGrid()

    with(charGrid) {
        partOne = points.filter { p ->
            this[p] == '#' && p.adjacentSides().all { this[it] == '#' }
        }.sumOf { (x, y) -> x * y }.s()
    }

    // Why manually? IDK
    val commands = (listOf(
        "A,B,A,C,A,A,C,B,C,B",
        "L,12,L,8,R,12",
        "L,10,L,8,L,12,R,12",
        "R,12,L,8,L,10",
        "n"
    ).joinToString("\n") + "\n").map { it.code.toLong() }
    partTwo = (listOf(2L) + prog.drop(1)).evalProgram(commands).s()
}