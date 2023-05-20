package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.bfsDistance

fun PuzzleSet.day6() = puzzle {
    data class Entry(val around: String, val orbiter: String)

    val entries = inputLines.map {
        val (around, orbiter) = it.split(")").asPair()
        Entry(around, orbiter)
    }

    val reverse = entries.associate { it.orbiter to it.around }
    fun String.orbits(): List<String> = reverse[this]?.let { listOf(it) + it.orbits() } ?: emptyList()

    val unique = entries.flatMap { listOf(it.around, it.orbiter) }.toSet()
    partOne = unique.sumOf { it.orbits().size }.s()

    val you = entries.first { it.orbiter == "YOU" }
    val san = entries.first { it.orbiter == "SAN" }

    val graph = unique.associateWith { from ->
        listOfNotNull(reverse[from]) + entries.filter { it.around == from }.map { it.orbiter }
    }

    partTwo = bfsDistance(you.around, isEnd = { it == san.around }, neighbors = { graph[it]!! }).s()
}