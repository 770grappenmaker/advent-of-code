package com.grappenmaker.aoc

import com.grappenmaker.aoc.year15.year2015
import com.grappenmaker.aoc.year16.year2016
import com.grappenmaker.aoc.year20.year2020
import com.grappenmaker.aoc.year21.year2021
import com.grappenmaker.aoc.year22.year2022
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset

const val aocURL = "https://adventofcode.com"

data class Puzzle(val year: Int, val day: Int, val implementation: SolveContext.() -> Unit) {
    val dayURL get() = "$aocURL/$year/day/$day"
    val inputURL get() = "$dayURL/input"
}

class SolveContext(val puzzle: Puzzle, val inputLines: List<String>) {
    val input by lazy { inputLines.joinToString("\n").trim() }
    val rawInput by lazy { inputLines.joinToString("\n") }

    var partOne = "Not implemented"
    var partTwo = "Not implemented"

    val formatted
        get() = """
        |Day ${puzzle.day} (${puzzle.dayURL})
        |Part 1: $partOne
        |Part 2: $partTwo
    """.trimMargin()

    // Utility to convert to string (felt shorter to use)
    fun <T> T.s() = toString()
}

data class PuzzleSet(val year: Int) {
    init {
        require(year >= 2015) { "What kind of ancient AOC did you find?" }
    }

    val puzzles = mutableListOf<Puzzle>()

    fun puzzle(day: Int = puzzles.size + 1, implementation: SolveContext.() -> Unit) {
        puzzles.add(Puzzle(year, day, implementation))
    }
}

// TODO: add more years here
val puzzles = listOf(year2015, year2016, year2020, year2021, year2022).flatMap { it.puzzles }

// Quote the official Advent of Code website:
// The first puzzles will unlock on December 1st at midnight EST (UTC-5). See you then!
val unlockOffset: ZoneOffset = ZoneOffset.ofHours(-5)

fun now(): OffsetDateTime = OffsetDateTime.now().withOffsetSameInstant(unlockOffset)
fun midnight(): OffsetDateTime = LocalDate.now(unlockOffset).atStartOfDay().atOffset(unlockOffset)

// Assumes november is the time to start prepping, I guess..?
fun eventYear() = now().let { if (it.month.value < 11) it.year - 1 else it.year }
fun isAdvent() = now().let { it.month == Month.DECEMBER && it.dayOfMonth in 1..25 }
fun eventDay() = if (isAdvent()) now().dayOfMonth else null