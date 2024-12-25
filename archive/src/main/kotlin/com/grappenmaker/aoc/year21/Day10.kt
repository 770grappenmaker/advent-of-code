package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day10() = puzzle(day = 10) {
    // Part one and two
    // Opening to closing
    val map = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val partOnePoints = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val partTwoPoints = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    val incorrectLines = mutableListOf<Char>()
    val incompleteLines = mutableListOf<List<Char>>()

    // Iterate over all lines, group them
    inputLines.forEach { line ->
        val stack = ArrayDeque(listOf(line.first()))

        var incorrectChar: Char? = null
        for (c in line) {
            if (c in map) {
                stack.add(c)
                continue
            }
            if (map[stack.last()] != c) {
                incorrectChar = c
                break
            } else stack.removeLast()
        }

        if (incorrectChar != null) {
            incorrectLines.add(incorrectChar)
        } else {
            incompleteLines.add(stack.toList().drop(1).map { map.getValue(it) })
        }
    }

    // Calculate scores for part one and two
    partOne = (incorrectLines.sumOf { partOnePoints.getValue(it) }).toString()

    val partTwoScores = incompleteLines.map {
        it.reversed().fold(0.toLong()) { acc, c -> (acc * 5L) + partTwoPoints.getValue(c).toLong() }
    }.sorted()

    partTwo = (partTwoScores[partTwoScores.size / 2]).toString()
}