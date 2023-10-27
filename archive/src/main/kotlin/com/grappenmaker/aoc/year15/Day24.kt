package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.permutations
import com.grappenmaker.aoc.product

fun PuzzleSet.day24() = puzzle(day = 24) {
    val packages = inputLines.map(String::toInt)
    val weightSum = packages.sum()

    fun solve(n: Int): String {
        val groupSum = weightSum / n
        return (1..packages.size).firstNotNullOf { r ->
            packages.permutations(r).filter { it.sum() == groupSum }.minOfOrNull { it.map(Int::toLong).product() }
        }.s()
    }

    partOne = solve(3)
    partTwo = solve(4)
}