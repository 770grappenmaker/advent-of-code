package com.grappenmaker.aoc.year15

import kotlin.math.abs
import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day5() = puzzle {
    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    val disallowed = listOf("ab", "cd", "pq", "xy")

    val partOneNice = inputLines.filter { str ->
        str.filter { it in vowels }.length >= 3 &&
                disallowed.none { it in str } &&
                str.windowedSequence(2).any { it[0] == it[1] }
    }

    partOne = partOneNice.size.s()

    // This code is naughty
    val partTwoNice = inputLines.filter { str ->
        val windowed = str.windowed(2).withIndex()
        windowed.any { (idxA, wA) ->
            windowed.any { (idxB, wB) -> abs(idxA - idxB) >= 2 && wA == wB }
        } && str.withIndex().any { (idx, c) ->
            str.getOrNull(idx + 2) == c
        }
    }

    partTwo = partTwoNice.size.s()
}