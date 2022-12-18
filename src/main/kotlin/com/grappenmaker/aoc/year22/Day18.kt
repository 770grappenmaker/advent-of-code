package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day18() = puzzle {
    data class Cube(val x: Int, val y: Int, val z: Int)
    operator fun Cube.plus(other: Cube) = Cube(x + other.x, y + other.y, z + other.z)
    operator fun Cube.minus(other: Cube) = Cube(x - other.x, y - other.y, z - other.z)

    val cubes = inputLines.map { l ->
        val (x, y, z) = l.splitInts()
        Cube(x, y, z)
    }

    val dirs = listOf(Cube(0, 1, 0), Cube(0, -1, 0), Cube(-1, 0, 0), Cube(1, 0, 0), Cube(0, 0, 1), Cube(0, 0, -1))
    partOne = cubes.sumOf { c -> dirs.count { c + it !in cubes } }.s()

    val lookAround = Cube(1, 1, 1)
    val min = Cube(cubes.minOf { it.x }, cubes.minOf { it.y }, cubes.minOf { it.z }) - lookAround
    val max = Cube(cubes.maxOf { it.x }, cubes.maxOf { it.y }, cubes.maxOf { it.z }) + lookAround

    val willFill = floodFill(Cube(0, 0, 0)) { c ->
        dirs.map { it + c }.filterNot { it in cubes }.filter { curr ->
            curr.x >= min.x && curr.y >= min.y && curr.z >= min.z &&
                    curr.x <= max.x && curr.y <= max.y && curr.z <= max.z
        }
    }

    partTwo = cubes.sumOf { c -> dirs.count { c + it in willFill } }.s()
}