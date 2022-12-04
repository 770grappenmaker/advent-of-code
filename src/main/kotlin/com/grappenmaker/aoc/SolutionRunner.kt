@file:JvmName("SolutionRunner")

package com.grappenmaker.aoc

import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import kotlin.io.path.absolutePathString
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val eventDay = eventDay()
    val eventYear = eventYear()
    val day = args.firstOrNull()?.toIntOrNull() ?: eventDay
        ?.also { println("Using today for the puzzle day ($it)") }
    ?: panic("Currently not AOC, specify a day to run!")

    val year = args.getOrNull(1)?.toInt()
        ?: eventYear.also { println("Using the 'current' AOC year ($it)") }

    val puzzle = puzzles.find { it.day == day && it.year == year }
        ?: panic("Unknown puzzle for day $day and year $year (not yet implemented?)")

    val inputFileName = args.getOrElse(2) { "day-${day.toString().padStart(2, '0')}.txt" }
    val inputsDir = Paths.get("inputs", year.toString())
    Files.createDirectories(inputsDir)

    val inputFile = inputsDir.resolve(inputFileName)
    if (!Files.exists(inputFile)) panic(
        """
        No input found (for file ${inputFile.absolutePathString()})
        Get your input here: ${puzzle.inputURL}
    """.trimIndent()
    )

    val input = inputFile.readLines()
    val context = SolveContext(puzzle, input)
    val timeTaken = measureNanoTime { puzzle.implementation(context) }

    println()
    println(context.formatted)
    println()
    println("Took ${timeTaken / 1_000_000}ms to calculate the solution")

    if (eventDay == day && year == eventYear) {
        val midnight = LocalDate.now(unlockOffset).atStartOfDay().atOffset(unlockOffset)
        val currentHour = now().hour
        val hoursUnlocked = currentHour - midnight.hour
        val nextIn = 24 - currentHour

        println()
        println("Puzzle has been unlocked for $hoursUnlocked hours")
        println("Next puzzle unlocks in $nextIn hours")
    }
}

fun panic(error: String): Nothing {
    println(error)
    exitProcess(-1)
}