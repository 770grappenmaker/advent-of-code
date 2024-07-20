package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.deinterlace
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day7() = puzzle {
    val parts = inputLines.map { it.split('[', ']').deinterlace() }
    partOne = parts.count { (norm, net) -> norm.any { it.hasAbba() } && net.none { it.hasAbba() } }.s()
    partTwo = parts.count { (norm, net) ->
        net.any { n -> n.filterABA().any { aba -> norm.any { it.hasMatchingBAB(aba) } } }
    }.s()
}

fun String.hasAbba() = deepen().windowed(4).any { (a1, b1, b2, a2) -> a1 == a2 && b1 == b2 && b1 != a1 }
fun String.filterABA() = windowed(3).filter { p -> p.deepen().let { (a1, b, a2) -> a1 == a2 && a1 != b } }
fun String.hasMatchingBAB(aba: String) = (aba[1].toString() + aba[0] + aba[1]) in this