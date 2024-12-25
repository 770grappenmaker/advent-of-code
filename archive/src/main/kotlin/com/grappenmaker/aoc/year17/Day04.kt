package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allDistinct
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day4() = puzzle {
    val passphrases = inputLines.map { it.split(" ") }
    partOne = passphrases.count { it.allDistinct() }.toString()
    partTwo = passphrases.count { p -> p.none { word -> (p - word).any { it.toSet() == word.toSet() } } }.toString()
}