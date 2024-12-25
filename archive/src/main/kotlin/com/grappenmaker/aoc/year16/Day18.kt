package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle(day = 18) {
    val parsed = input.map { it == '^' }

    fun solve(size: Int): Int {
        var curr = parsed
        var sum = curr.count { !it }

        repeat(size - 1) {
            curr = buildList {
                for (x in curr.indices) {
                    val left = curr.getOrNull(x - 1) ?: false
                    val center = curr.getOrNull(x) ?: false
                    val right = curr.getOrNull(x + 1) ?: false

                    val rule1 = left && center && !right
                    val rule2 = !left && center && right
                    val rule3 = left && !center && !right
                    val rule4 = !left && !center && right
                    val trap = rule1 || rule2 || rule3 || rule4

                    if (!trap) sum++
                    add(trap)
                }
            }
        }

        return sum
    }

    partOne = solve(40).toString()
    partTwo = solve(400000).toString()
}