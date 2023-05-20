package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*

fun PuzzleSet.day8() = puzzle(day = 8) {
    val width = 25
    val height = 6

    val layers = input.deepen().map { it.toString().toInt() }.windowed(width, width).windowed(height, height)
    val flattenedLayers = layers.map { it.flatten() }
    val p1Layer = flattenedLayers.minBy { it.countContains(0) }
    partOne = (p1Layer.countContains(1) * p1Layer.countContains(2)).s()

    val grids = layers.map { it.asGrid() }
    partTwo = "\n" + grids.first().points.map { p -> grids.map { it[p] }
        .firstOrNull { it != 2 } == 1 }.asGrid(width).debug(on = "oo ", off = "   ")
}