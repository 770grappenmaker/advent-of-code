package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allDistinct

fun PuzzleSet.day4() = puzzle {
    val passphrases = inputLines.map { it.split(" ") }
    partOne = passphrases.count { it.allDistinct() }.s()
    partTwo = passphrases.count { p -> p.none { word -> (p - word).any { it.toSet() == word.toSet() } } }.s()
}