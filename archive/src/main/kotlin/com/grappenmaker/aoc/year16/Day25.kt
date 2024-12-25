package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    // Probably the ugliest AOC code so far
    partOne = generateSequence(1L, Long::inc).first { i ->
        VM(inputLines.parseProgram()).apply {
            var pattern = false
            var outputs = 0
            registers['a'] = i

            stepUntilHalted {
                when {
                    // I love heuristics
                    outputs == 10 -> return@first true
                    pattern != ((output.removeFirstOrNull() ?: return@stepUntilHalted) == 1L) -> halted = true
                    else -> {
                        pattern = !pattern
                        outputs++
                    }
                }
            }
        }

        false
    }.toString()
}