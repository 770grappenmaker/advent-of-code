package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year21.generateSequenceIndexed
import com.grappenmaker.aoc.year22.firstNotDistinct

fun PuzzleSet.day1() = puzzle {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sum().s()
    partTwo = generateSequenceIndexed(0) { idx, acc -> acc + nums[idx % nums.size] }.asIterable().firstNotDistinct().s()
}