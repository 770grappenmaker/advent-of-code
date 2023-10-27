package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.permPairs

fun PuzzleSet.day2() = puzzle {
    val counts = inputLines.deepen().map { w -> w.groupingBy { it }.eachCount().values }
    partOne = (counts.count { 2 in it } * counts.count { 3 in it }).s()

    val (l, r) = inputLines.permPairs().filter { (l, r) -> l != r }
        .minBy { (l, r) -> l.zip(r).count { (a, b) -> a != b } }

    partTwo = (l.deepen().filterIndexed { idx, c -> r[idx] == c }).joinToString("")
}