package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    // This will probably not work for all inputs
    // I bet only the item names will differ, or maybe the directions..
    // I hope the dangerous item names are identical
    // you could probably write a flood fill or something, but basic like this,
    // is probably fine. Ha, better solve this problem by hand
    val job = """
    east
    take coin
    west
    west
    take mug
    east
    east
    north
    north
    take hypercube
    south
    south
    south
    west
    take astrolabe
    north
    east
    north
    east
    """.trimIndent().lines().flatMap { it.toList().map(Char::code).map(Int::toLong) + 10L }
    
    partOne = startComputer(input, job).also { it.runUntilHalt() }.output
        .joinToString("") { it.toInt().toChar().toString() }.trim().lines().last().splitInts().max().s()
}