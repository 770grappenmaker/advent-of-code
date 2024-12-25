package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day4() = puzzle {
    val pairs = inputLines.map { l -> l.split(",").flatMap { it.split("-").map(String::toInt) } }
    partOne = pairs.count { (l1, r1, l2, r2) -> l1 >= l2 && r1 <= r2 || l2 >= l1 && r2 <= r1 }.toString()
    partTwo = pairs.count { (l1, r1, l2, r2) -> !(r1 < l2 || r2 < l1) }.toString()
}