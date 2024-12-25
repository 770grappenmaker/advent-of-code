package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day3() = puzzle(day = 3) {
    val g = inputLines.asCharGrid()
    fun Point.seq(dir: Direction) = generateSequence(this) { it + dir }.takeWhile { it in g && g[it].isDigit() }

    val numbers = g.pointsSequence
        .filter { g[it].isDigit() && with(g) { it.allAdjacent() }.any { p -> g[p] != '.' && !g[p].isDigit() } }
        .mapTo(hashSetOf()) { it.seq(LEFT).last() }.map { it.seq(RIGHT) }
        .map { it to inputLines[it.first().y].substring(it.first().x..it.last().x).toInt() }

    partOne = numbers.sumOf { (_, n) -> n }.toString()
    partTwo = numbers.groupBy { (n) ->
        n.firstNotNullOfOrNull { with(g) { it.allAdjacent() }.singleOrNull { g[it] == '*' } }
    }.filterKeysNotNull().values.filter { it.size == 2 }.sumOf { (a, b) -> a.second * b.second }.toString()
}