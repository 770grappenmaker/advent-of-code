@file:JvmName("SolutionRunner")

package com.grappenmaker.aoc

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.absolutePathString
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val day = args.firstOrNull()?.toIntOrNull() ?: eventDay()
        ?.also { println("Using today for the puzzle day ($it)") }
    ?: panic("Currently not AOC, specify a day to run!")

    val year = args.getOrNull(1)?.toInt()
        ?: eventYear().also { println("Using the 'current' AOC year ($it)") }

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
    val timeTaken = measureTimeMillis { puzzle.implementation(context) }

    println()
    println(context.formatted)
    println()
    println("Took ${timeTaken}ms to calculate the solution")
}

fun panic(error: String): Nothing {
    println(error)
    exitProcess(-1)
}