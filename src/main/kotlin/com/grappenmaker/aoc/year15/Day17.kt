package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day17() = puzzle {
    val containerSizes = inputLines.map { it.toInt() }

    val totalSize = 150
    val foundSolutions = hashSetOf<Set<Int>>()
    val seen = hashSetOf<Set<Int>>(emptySet())

    // Backtracking-ish
    // Slow (~30 seconds), but gets the job done.
    // Worst-case scenario all elements match which gives O(n!)
    // I feel like there should be a better algorithm, but it does the job
    fun recurse(current: Set<Int>) {
        val currentEval = current.sumOf { containerSizes[it] }

        when {
            currentEval > totalSize -> return
            currentEval == totalSize -> {
                foundSolutions.add(current)
                return
            }
            else -> (containerSizes.indices - current).map { current + it }
                .filter { seen.add(it) }.forEach { recurse(it) }
        }
    }

    recurse(emptySet())
    partOne = foundSolutions.size.s()

    // Well, this part was easy?
    val bestSolution = foundSolutions.minOf { it.size }
    partTwo = foundSolutions.count { it.size == bestSolution }.s()
}