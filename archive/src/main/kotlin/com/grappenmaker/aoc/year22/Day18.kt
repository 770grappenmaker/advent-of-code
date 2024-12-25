package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day18() = puzzle {
    val cubes = inputLines.map { l ->
        val (x, y, z) = l.splitInts()
        Point3D(x, y, z)
    }.toSet()

    partOne = cubes.sumOf { c -> c.adjacentSides().count { it !in cubes } }.toString()

    val lookAround = Point3D(1, 1, 1)
    val area = (cubes.minBound() - lookAround)..(cubes.maxBound() + lookAround)
    val willFill = floodFill(Point3D(0, 0, 0)) { c -> c.adjacentSides().filter { it !in cubes && it in area } }

    partTwo = cubes.sumOf { c -> c.adjacentSides().count { it in willFill } }.toString()
}