package com.grappenmaker.aoc

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.nio.file.Path
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.reflect.KProperty
import kotlin.system.measureNanoTime

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

    inner class PartDelegate(part: Int) {
        var underlying =
            if (part == 2 && puzzle.day == 25) "Merry Christmas! And a Happy New Year!" else "Not implemented"

        var touched = false
            private set

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = underlying
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
            touched = true
            underlying = value.toString()
        }
    }

    val partOneDelegate = PartDelegate(1)
    val partTwoDelegate = PartDelegate(2)
    var partOne: Any by partOneDelegate
    var partTwo: Any by partTwoDelegate
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

fun runPuzzle(puzzle: Puzzle, input: List<String>) {
    println("Day ${puzzle.day} (${puzzle.dayURL})")

    val eventDay = eventDay()
    val eventYear = eventYear()
    val context = SolveContext(puzzle, input)
    val timeTaken = measureNanoTime { puzzle.implementation(context) }

    println()
    println(
        """
        |Part 1: ${context.partOneDelegate.underlying}
        |Part 2: ${context.partTwoDelegate.underlying}
        """.trimMargin()
    )
    println()
    println("Took ${"%.1f".format(timeTaken / 1_000_000.0)}ms to calculate the solution")

    listOf(context.partOneDelegate, context.partTwoDelegate).findLast { it.touched }?.let { d ->
        runCatching {
            val selection = StringSelection(d.underlying)
            Toolkit.getDefaultToolkit().systemClipboard.setContents(selection, selection)

            println("Copied \"${d.underlying}\" to clipboard")
        }.onFailure { println("Failed to copy to clipboard!") }
    }

    if (eventDay == puzzle.day && puzzle.year == eventYear) {
        val midnight = LocalDate.now(unlockOffset).atStartOfDay().atOffset(unlockOffset)
        val currentHour = now().hour
        val hoursUnlocked = currentHour - midnight.hour
        val nextIn = 24 - currentHour

        println()
        println("Puzzle has been unlocked for $hoursUnlocked hours")
        println("Next puzzle unlocks in $nextIn hours")
    }
}

fun defaultInput(year: Int, day: Int, title: String = "day"): Path = inputsDir(year).resolve(inputName(day, title))

fun simplePuzzle(
    day: Int,
    year: Int,
    input: Path = defaultInput(year, day),
    block: SolveContext.() -> Unit
) = runPuzzle(Puzzle(year, day, block), input.readLines())