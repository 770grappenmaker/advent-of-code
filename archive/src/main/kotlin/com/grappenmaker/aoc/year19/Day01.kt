package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day1() = puzzle {
    val nums = inputLines.map(String::toInt)
    partOne = nums.sumOf { (it / 3) - 2 }.s()
    partTwo = nums.sumOf { num ->
        generateSequence(num) { (it / 3) - 2 }.drop(1).takeWhile { it > 0 }.sum()
    }.s()
}