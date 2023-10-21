package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PointND
import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.floodFill
import com.grappenmaker.aoc.manhattanDistanceTo

fun PuzzleSet.day25() = puzzle {
    val coords = inputLines.map { PointND(it.split(',').map(String::toInt)) }
    val left = coords.toHashSet()
    var constellations = 0

    while (left.isNotEmpty()) {
        left -= floodFill(left.first(), neighbors = { curr -> left.filter { it.manhattanDistanceTo(curr) <= 3 } })
        constellations++
    }

    partOne = constellations.s()
}