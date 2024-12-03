package com.grappenmaker.aoc

import java.nio.file.Path
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.io.path.Path
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

class PartDelegate<T : Any>(default: T) {
    var underlying: T = default
    var touched = false
        private set

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = underlying
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        touched = true
        underlying = value
    }
}

class SolveContext(puzzle: Puzzle, val inputLines: List<String>) {
    val input by lazy { inputLines.joinToString("\n").trim() }
    val rawInput by lazy { inputLines.joinToString("\n") }

    var partOneDelegate = PartDelegate<Any>("Not implemented")
    var partTwoDelegate = PartDelegate<Any>(
        if (puzzle.day == 25) "Merry Christmas! And a Happy New Year!" else "Not implemented"
    )

    var partOne: Any by partOneDelegate
    var partTwo: Any by partTwoDelegate

    @Suppress("UNCHECKED_CAST") // very bad, i know
    inline fun <reified T : Any> overwritePartOne(default: T): PartDelegate<T> =
        PartDelegate(default).also { partOneDelegate = it as PartDelegate<Any> }

    @Suppress("UNCHECKED_CAST") // very bad, i know
    inline fun <reified T : Any> overwritePartTwo(default: T): PartDelegate<T> =
        PartDelegate(default).also { partTwoDelegate = it as PartDelegate<Any> }
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

// Quote the official Advent of Code website:
// The first puzzles will unlock on December 1st at midnight EST (UTC-5). See you then!
val unlockOffset: ZoneOffset = ZoneOffset.ofHours(-5)

fun now(): OffsetDateTime = OffsetDateTime.now().withOffsetSameInstant(unlockOffset)
fun midnight(): OffsetDateTime = LocalDate.now(unlockOffset).atStartOfDay().atOffset(unlockOffset)

// Assumes november is the time to start prepping, I guess..?
fun eventYear() = now().let { if (it.month.value < 11) it.year - 1 else it.year }
fun isAdvent() = now().let { it.month == Month.DECEMBER && it.dayOfMonth in 1..25 }
fun eventDay() = if (isAdvent()) now().dayOfMonth else null

fun inputsDir(year: Int): Path = Path("inputs", year.toString())
fun inputName(day: Int, title: String = "day") = "$title-${day.toString().padStart(2, '0')}.txt"