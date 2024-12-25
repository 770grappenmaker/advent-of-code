package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(7) {
    val rules = inputLines.associate { l ->
        val (bag, contains) = l.split(" contain ")
        val bagType = bag.removePlural()
        bagType to contains.removeSuffix(".").split(", ").filterNot { it.startsWith("no") }
            .associate { c -> c.drop(2).removePlural() to c.first().digitToInt() }
    }

    fun expand(type: String): Set<String> {
        val keys = rules.getValue(type).keys
        return keys + keys.flatMap { expand(it) }
    }

    fun findSum(type: String): Int = rules.getValue(type).toList().sumOf { (t, a) -> a * (findSum(t) + 1) }

    partOne = rules.keys.map { expand(it) }.count { "shiny gold bag" in it }.toString()
    partTwo = findSum("shiny gold bag").toString()
}

fun String.removePlural() = if (endsWith('s')) dropLast(1) else this