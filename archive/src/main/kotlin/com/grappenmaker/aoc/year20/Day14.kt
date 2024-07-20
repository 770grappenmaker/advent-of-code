package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    // Don't think I can generalize here
    fun solve(solution: (memory: MutableMap<Long, Long>) -> Unit) =
        mutableMapOf<Long, Long>().also(solution).values.sum().s()

    fun solvePartOne() = solve { memory ->
        var andMask = 0xFFFFFFFFFL
        var orMask = 0L

        inputLines.forEach { l ->
            val (toSet, valuePart) = l.split(" = ")
            if (toSet == "mask") {
                andMask = 0xFFFFFFFFFL
                orMask = 0L
                valuePart.reversed().forEachIndexed { idx, c ->
                    when (c) {
                        '1' -> orMask = orMask or (1L shl idx)
                        '0' -> andMask = andMask xor (1L shl idx)
                    }
                }
            } else {
                memory[toSet.drop(4).dropLast(1).toLong()] = valuePart.toLong() and andMask or orMask
            }
        }
    }

    fun solvePartTwo() = solve { memory ->
        var orMask = 0L
        val basicPerms = listOf(0xFFFFFFFFFL to 0L)
        var permutations = basicPerms

        inputLines.forEach { l ->
            val (toSet, valuePart) = l.split(" = ")
            if (toSet == "mask") {
                orMask = 0L
                permutations = basicPerms
                valuePart.reversed().forEachIndexed { idx, c ->
                    val shifted = 1L shl idx
                    when (c) {
                        '1' -> orMask = orMask or shifted
                        'X' -> permutations = permutations
                            .flatMap { (a, o) -> listOf(a to (o or shifted), (a xor shifted) to o) }
                    }
                }
            } else {
                val updated = toSet.drop(4).dropLast(1).toLong() or orMask
                val value = valuePart.toLong()
                permutations.forEach { (a, o) -> memory[updated and a or o] = value }
            }
        }
    }

    partOne = solvePartOne()
    partTwo = solvePartTwo()
}