package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.sign

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(day = 12) {
    data class MoonInfo(val pos: Point3D, val velo: Point3D = Point3D(0, 0, 0))
    fun MoonInfo.energy() = pos.manhattanDistance * velo.manhattanDistance

    val initialMoons = inputLines.map {
        val (x, y, z) = it.splitInts()
        MoonInfo(Point3D(x, y, z))
    }

    fun seq() = generateSequence(initialMoons) { moons ->
        moons.map { (pos, velo) ->
            val newVelo = moons.fold(velo) { acc, (curr) -> acc + (curr - pos).map { it.sign } }
            MoonInfo(pos + newVelo, newVelo)
        }
    }

    partOne = seq().nth(1000).sumOf { it.energy() }.toString()

    fun findRep(extract: (Point3D) -> Int) = seq().map { it.map { (pos, velo) -> extract(pos) to extract(velo) } }
        .withIndex().firstNotDistinctBy { it.value }.index.toLong()

    partTwo = lcmOf(findRep(Point3D::x), findRep(Point3D::y), findRep(Point3D::z)).toString()
}