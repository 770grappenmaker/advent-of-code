package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Point
import com.grappenmaker.aoc.year22.adjacentSidesInf
import com.grappenmaker.aoc.year22.dijkstra
import com.grappenmaker.aoc.year22.floodFill

fun PuzzleSet.day13() = puzzle(13) {
    val no = input.toInt()
    fun isValid(p: Point) = p.x >= 0 && p.y >= 0 &&
            (p.x * p.x + 3 * p.x + 2 * p.x * p.y + p.y + p.y * p.y + no).countOneBits() % 2 == 0

    // Hate the fact that my bfs is broken D:
    fun steps(start: Point, end: Point) = dijkstra(
        start,
        isEnd = { it == end },
        neighbors = { p -> p.adjacentSidesInf().filter(::isValid) },
        findCost = { 1 }
    )!!.cost

    val start = Point(1, 1)
    partOne = steps(start, Point(31, 39)).s()
    partTwo = generateSequence(setOf(start)) { s -> s.flatMap { it.adjacentSidesInf().filter(::isValid) }.toSet() }
        .take(51).flatten().distinct().count().s()
}