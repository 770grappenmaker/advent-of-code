package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year21.generateSequenceIndexed
import com.grappenmaker.aoc.year22.firstNotDistinct

fun main() = simplePuzzle(1, 2018) {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sum().s()
    partTwo = generateSequenceIndexed(0) { idx, acc -> acc + nums[idx % nums.size] }.asIterable().firstNotDistinct().s()
}