package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(9, 2025) {
    val pts = inputLines.map { it.split(",").map(String::toInt).asPair().toPoint() }
    val combs = pts.combinations(2).map { (a, b) -> Rectangle(a, b).reorder() }.sortedByDescending { it.areaLong }
    partOne = combs.first().areaLong

    val lines = (pts + pts.first()).windowed(2) { (a, b) -> (a..b).reorder() }
    partTwo = combs.first { rect -> lines.none { rect.intersects(it) } }.areaLong
}