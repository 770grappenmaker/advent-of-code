package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(day = 21) {
    data class Rule(val inputs: Set<BooleanGrid>, val output: BooleanGrid)
    fun String.parseRuleGrid() = split("/").asGrid { it == '#' }

    val start = """
        .#.
        ..#
        ###
    """.trimIndent().lines().asGrid { it == '#' }

    val rules = inputLines.map { l ->
        val (i, o) = l.split(" => ").map(String::parseRuleGrid)
        Rule(i.orientations(), o)
    }.groupBy { it.inputs.first().width }

    fun solve(n: Int) = generateSequence(start) { grid ->
        val partsSize = if (grid.width % 2 == 0) 2 else 3
        val toMatch = rules.getValue(partsSize)

        grid.rowsValues.chunked(partsSize) { verticalChunk ->
            verticalChunk.swapOrder().chunked(partsSize) { chunk ->
                val original = chunk.asGrid()
                toMatch.first { original in it.inputs }.output.rowsValues
            }.reduce { acc, curr -> acc + curr }.swapOrder()
        }.reduce { acc, curr -> acc + curr }.asGrid()
    }.nth(n).countTrue().toString()

    partOne = solve(5)
    partTwo = solve(18)
}