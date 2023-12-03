package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day3() = puzzle(day = 3) {
    val g = inputLines.asCharGrid()
    val numbers = g.findPoints { it.isDigit() }.asSequence()
        .filter { with(g) { it.allAdjacent() }.any { p -> g[p] != '.' && !g[p].isDigit() } }
        .mapTo(hashSetOf()) { p -> generateSequence(p) { it + LEFT }.takeWhile { it in g && g[it].isDigit() }.last() }
        .map { s -> floodFill(s) { listOfNotNull((it + RIGHT).takeIf { p -> p in g && g[p].isDigit() }) } }
        .map { ps ->
            ps to ps.sortedByDescending { it.x }.foldIndexed(0) { i, a, c -> a + g[c].digitToInt() * 10.pow(i) }
        }.toList()

    partOne = numbers.sumOf { (_, n) -> n }.s()

    val gears = g.findPointsValued('*').toSet()
    partTwo = numbers.groupBy { (n) ->
        n.flatMapTo(hashSetOf()) { with(g) { it.allAdjacent() } }.intersect(gears).singleOrNull()
    }.filterKeysNotNull().values.filter { it.size == 2 }.sumOf { it.map { (_, n) -> n }.product() }.s()
}