package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day23() = puzzle(day = 23) {
    // Very nice... took a while of reading code to see that this easily boils down to a factorial,
    // offset by (73-1)*(73-2)=5112, for my input. Here is my bruteforce solution, though...
    // Takes about a minute to run on my machine.
    // Proper solution:
    // partX = (n.factorial() + 5112).s()
    fun solve(n: Long) = VM(inputLines.parseProgram()).run {
        registers['a'] = n
        stepUntilHalted()
        registers['a'].s()
    }

    partOne = solve(7)
    partTwo = solve(12)
}