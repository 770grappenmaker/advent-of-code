package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(day = 9) {
    val seq = inputLines.map { ns ->
        generateSequence(ns.split(" ").map(String::toInt)) { c -> c.windowed(2) { (a, b) -> b - a } }
            .takeUntil { it.any { c -> c != 0 } }.toList()
    }

    partOne = seq.sumOf { s -> s.sumOf { it.last() } }.toString()
    partTwo = seq.sumOf { s -> s.foldRight(0L) { c, a -> c.first() - a } }.toString()
}