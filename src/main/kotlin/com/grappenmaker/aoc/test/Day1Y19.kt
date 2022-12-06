package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle

fun main() = simplePuzzle(1, 2019) {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sumOf { (it / 3) - 2 }.s()
    partTwo = nums.sumOf { num ->
        generateSequence(num) { (it / 3) - 2 }.drop(1).takeWhile { it > 0 }.sum()
    }.s()
}