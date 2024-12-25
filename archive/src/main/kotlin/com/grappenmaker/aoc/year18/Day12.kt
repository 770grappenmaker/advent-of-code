package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.nth

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(day = 12) {
    fun String.parse() = map { it == '#' }.withIndex().filter { it.value }.map { it.index }

    val (initPart, rulesPart) = input.split("\n\n")
    val initial = initPart.substringAfter(": ").parse()

    data class Rule(val pattern: List<Int>, val to: Boolean)
    val rules = rulesPart.lines().map { l ->
        val (pattern, to) = l.split(" => ")
        Rule(pattern.parse().map { it - 2 }, to == "#")
    }

    fun List<Int>.next(): List<Int> {
        val result = mutableListOf<Int>()

        for (idx in first() - 2..last() + 2) {
            val currentPattern = (-2..2).filter { it + idx in this }
            if (rules.find { (pattern) -> currentPattern == pattern }?.to == true) result += idx
        }

        return result
    }

    fun seq() = generateSequence(initial) { it.next() }
    partOne = seq().nth(20).sumOf { it.toLong() }.toString()

    fun checkRepeat(old: List<Int>, new: List<Int>) = old.size == new.size &&
            old.mapIndexed { idx, v -> new[idx] - v }.toSet().size == 1

    val (old, new) = seq().withIndex().zipWithNext().first { (a, b) -> checkRepeat(a.value, b.value) }
    val shift = new.value.first() - old.value.first()
    val iterationsLeft = 50000000000 - new.index
    partTwo = new.value.sumOf { it.toLong() + (shift.toLong() * iterationsLeft) }.toString()
}