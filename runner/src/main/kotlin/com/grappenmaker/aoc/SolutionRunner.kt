@file:JvmName("SolutionRunner")

package com.grappenmaker.aoc

import com.grappenmaker.aoc.years.puzzles
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import kotlin.io.path.absolutePathString
import kotlin.io.path.readLines
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    val eventDay = eventDay()
    val eventYear = eventYear()
    val day = args.firstOrNull()?.toIntOrNull() ?: eventDay
        ?.also { println("Using today for the puzzle day ($it)") }
    ?: panic("Currently not AOC, specify a day to run!")

    val year = args.getOrNull(1)?.toInt()
        ?: eventYear.also { println("Using the 'current' AOC year ($it)") }

    // obviously bad code but like, I don't really want to over optimize
    val puzzle = puzzles.find { it.day == day && it.year == year }
        ?: panic("Unknown puzzle for day $day and year $year (not yet implemented?)")

    val inputFileName = args.getOrElse(2) { inputName(day) }
    val inputsDir = inputsDir(year)
    Files.createDirectories(inputsDir)

    val inputFile = inputsDir.resolve(inputFileName)
    if (!Files.exists(inputFile)) panic(
        """
        No input found (for file ${inputFile.absolutePathString()})
        Get your input here: ${puzzle.inputURL}
    """.trimIndent()
    )
    println()

    runPuzzle(puzzle, inputFile.readLines())
}

fun panic(error: String): Nothing {
    println(error)
    exitProcess(-1)
}

fun runPuzzle(puzzle: Puzzle, input: List<String>) {
    println("Day ${puzzle.day} (${puzzle.dayURL})")

    val eventDay = eventDay()
    val eventYear = eventYear()
    val context = SolveContext(puzzle, input)
    val timeTaken = measureNanoTime { puzzle.implementation(context) }

    println()
    println(
        """
        |Part 1: ${context.partOne}
        |Part 2: ${context.partTwo}
        """.trimMargin()
    )
    println()
    println("Took ${timeTaken / 1_000_000}ms to calculate the solution")

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