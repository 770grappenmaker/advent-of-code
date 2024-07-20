package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.permPairsExclusive

const val preambleSize = 25

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(9) {
    val nums = inputLines.map(String::toLong)
    val other = nums.drop(preambleSize)
    val invalid = other.filterIndexed { idx, v ->
        v !in nums.subList(idx, idx + preambleSize).permPairsExclusive().map { (a, b) -> a + b }
    }.first()

    partOne = invalid.s()
    val range = generateSequence(2) { it + 1 }.firstNotNullOf { nums.windowed(it).find { w -> w.sum() == invalid } }
        .sorted()

    partTwo = (range.first() + range.last()).s()
}