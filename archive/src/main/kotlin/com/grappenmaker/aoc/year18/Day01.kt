package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.firstNotDistinct
import com.grappenmaker.aoc.repeatInfinitely

fun PuzzleSet.day1() = puzzle {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sum().s()
    partTwo = nums.asSequence().repeatInfinitely().runningReduce { a, i -> a + i }.asIterable().firstNotDistinct().s()
}