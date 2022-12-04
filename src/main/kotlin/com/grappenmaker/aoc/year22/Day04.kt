package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day4() = puzzle {
    val pairs = inputLines.map { it.split(",").flatMap { p -> p.split("-").map(String::toInt) } }
    partOne = pairs.count { (l1, r1, l2, r2) -> (l1 >= l2 && r1 <= r2) || (l2 >= l1 && r2 <= r1) }.s()
    partTwo = pairs.count { (l1, r1, l2, r2) -> l1 in l2..r2 || r1 in l2..r2 || l2 in l1..r1 || r2 in l1..r1 }.s()
}