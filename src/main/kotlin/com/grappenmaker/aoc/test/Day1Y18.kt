package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year21.generateSequenceIndexed

fun main() = simplePuzzle(1, 2018) {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sum().s()

    val set = hashSetOf<Int>()
    partTwo = generateSequenceIndexed(0) { idx, acc -> acc + nums[idx % nums.size] }.first { !set.add(it) }.s()
}