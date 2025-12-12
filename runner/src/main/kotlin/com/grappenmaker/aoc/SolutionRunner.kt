@file:JvmName("SolutionRunner")

package com.grappenmaker.aoc

import com.grappenmaker.aoc.years.year2025
import java.nio.file.Files
import kotlin.io.path.absolutePathString
import kotlin.io.path.readLines
import kotlin.system.exitProcess

val years = listOf(year2025)
val puzzles = years.flatMap { it.puzzles }

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