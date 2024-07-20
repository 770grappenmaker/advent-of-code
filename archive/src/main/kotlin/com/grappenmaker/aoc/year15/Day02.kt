package com.grappenmaker.aoc.year15

import kotlin.math.min
import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day2() = puzzle {
    val cuboids = inputLines.map {
        val (length, width, height) = it.split("x").map(String::toInt)
        Cuboid(length, width, height)
    }

    partOne = cuboids.sumOf { it.totalPaper }.s()
    partTwo = cuboids.sumOf { it.totalRibbon }.s()
}

data class Cuboid(val length: Int, val width: Int, val height: Int)

val Cuboid.lwSide get() = length * width
val Cuboid.whSide get() = width * height
val Cuboid.hlSide get() = height * length
val Cuboid.extraPaper get() = min(lwSide, min(whSide, hlSide))
val Cuboid.surfaceArea get() = 2 * lwSide + 2 * whSide + 2 * hlSide
val Cuboid.totalPaper get() = surfaceArea + extraPaper
val Cuboid.bowLength get() = width * height * length
val Cuboid.wrapRibbon: Int
    get() {
        val (a, b) = listOf(length, width, height).sorted()
        return 2 * a + 2 * b
    }

val Cuboid.totalRibbon get() = bowLength + wrapRibbon