package com.grappenmaker.aoc

import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val aocURL = "https://adventofcode.com"

data class Puzzle(
    val year: Int,
    val day: Int,
    val implementation: SolveContext.() -> Unit
) {
    val dayURL get() = "$aocURL/$year/day/$day"
    val inputURL get() = "$dayURL/input"
}

class SolveContext(val puzzle: Puzzle, val inputLines: List<String>) {
    val input by lazy { inputLines.joinToString("\n").trim() }
    val rawInput by lazy { inputLines.joinToString("\n") }

    inner class PartDelegate(part: Int) : ReadWriteProperty<Any?, String> {
        var underlying =
            if (part == 2 && puzzle.day == 25) "Merry Christmas! And a Happy New Year!" else "Not implemented"

        var touched = false
            private set

        override fun getValue(thisRef: Any?, property: KProperty<*>) = underlying
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            touched = true
            underlying = value
        }
    }

    val partOneDelegate = PartDelegate(1)
    val partTwoDelegate = PartDelegate(2)
    var partOne by partOneDelegate
    var partTwo by partTwoDelegate

//    var partOne = "Not implemented"
//    var partTwo = "Not implemented"

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

// jank!
val years by lazy { (15..23).map { generateYear(2000 + it, "com/grappenmaker/aoc/year$it") } }

val puzzles by lazy { years.flatMap { it.puzzles } }

// Quote the official Advent of Code website:
// The first puzzles will unlock on December 1st at midnight EST (UTC-5). See you then!
val unlockOffset: ZoneOffset = ZoneOffset.ofHours(-5)

fun now(): OffsetDateTime = OffsetDateTime.now().withOffsetSameInstant(unlockOffset)
fun midnight(): OffsetDateTime = LocalDate.now(unlockOffset).atStartOfDay().atOffset(unlockOffset)

// Assumes november is the time to start prepping, I guess..?
fun eventYear() = now().let { if (it.month.value < 11) it.year - 1 else it.year }
fun isAdvent() = now().let { it.month == Month.DECEMBER && it.dayOfMonth in 1..25 }
fun eventDay() = if (isAdvent()) now().dayOfMonth else null