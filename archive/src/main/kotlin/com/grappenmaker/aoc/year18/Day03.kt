package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day3() = puzzle {
    val rects = inputLines.map { l ->
        val split = l.split(" ")
        val (x, y) = split[2].removeSuffix(":").split(",").map(String::toInt)
        val (w, h) = split[3].split("x").map(String::toInt)
        sizedRect(w, h, x, y)
    }

    partOne = rects.flatMap { it.points }.notDistinct().distinct().size.s()
    partTwo = (rects.indexOfFirst { rect -> (rects - rect).none { it.overlapsInclusive(rect) } } + 1).s()
}