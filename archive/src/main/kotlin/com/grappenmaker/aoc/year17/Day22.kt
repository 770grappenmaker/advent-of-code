package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day22() = puzzle(day = 22) {
    val initialGrid = inputLines.asGrid { it == '#' }
    val initialAntPos = Point(initialGrid.width / 2, initialGrid.height / 2)

    fun solve(n: Int, step: Int): String {
        // 0=clean
        // 1=weakened
        // 2=infected
        // 3=flagged
        val states = initialGrid.filterTrue().associateWith { 2 }.toMutableMap()
        var antPos = initialAntPos
        var antDir = UP
        var ans = 0

        repeat(n) {
            val currState = states[antPos] ?: 0
            antDir = antDir.next(currState - 1)
            states[antPos] = ((currState + step) % 4).also { if (it == 2) ans++ }
            antPos += antDir
        }

        return ans.s()
    }

    partOne = solve(10000, 2)
    partTwo = solve(10000000, 1)
}