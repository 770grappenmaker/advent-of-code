package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.firstNotDistinct
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.repeatInfinitely

@PuzzleEntry
fun PuzzleSet.day1() = puzzle {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sum().toString()
    partTwo = nums.asSequence().repeatInfinitely().runningReduce { a, i -> a + i }.asIterable().firstNotDistinct().toString()
}