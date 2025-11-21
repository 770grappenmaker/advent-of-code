package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.y25day03() {
    partOne = partOneInput.input.ints().distinct().sum()
    partTwo = partTwoInput.input.ints().distinct().sorted().take(20).also { println(it) }.sum()
    partThree = partThreeInput.input.ints().groupingBy { it }.eachCount().values.max()
}