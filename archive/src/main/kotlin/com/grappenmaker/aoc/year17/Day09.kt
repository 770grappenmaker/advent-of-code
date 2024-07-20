package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(day = 9) {
    // Pretty ewey non-kotlin'y code, but F A S T
    var idx = 0
    var garbage = false
    var open = 0
    var partOneCount = 0
    var partTwoCount = 0

    while (idx in input.indices) {
        val wasGarbage = garbage
        val curr = input[idx]

        if (curr == '<') garbage = true
        if (garbage && curr == '!') {
            idx += 2
            continue
        }

        if (curr == '>') garbage = false
        when {
            !garbage -> when {
                curr == '{' -> open++
                curr == '}' && open > 0 -> {
                    partOneCount += open
                    open--
                }
            }
            wasGarbage -> partTwoCount++
        }

        idx++
    }

    partOne = partOneCount.s()
    partTwo = partTwoCount.s()
}