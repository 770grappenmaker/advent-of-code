package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.doubleLines
import com.grappenmaker.aoc.lcm
import com.grappenmaker.aoc.repeatInfinitely

fun PuzzleSet.day8() = puzzle(day = 8) {
    val (insns, grid) = input.doubleLines()
    val dirs = insns.map { if (it == 'L') 0 else 1 }
    val graph = grid.lines().associate { l ->
        val (a, b) = l.split(" = ")
        a to b.drop(1).dropLast(1).split(", ")
    }

    fun solve(start: List<String>, partTwo: Boolean) = start.map { p ->
        dirs.asSequence().repeatInfinitely().scan(p) { a, c -> graph.getValue(a)[c] }
            .indexOfFirst { if (partTwo) it.last() == 'Z' else it == "ZZZ" }.toLong()
    }.lcm().s()

    partOne = solve(listOf("AAA"), false)
    partTwo = solve(graph.keys.filter { it.last() == 'A' }, true)
}