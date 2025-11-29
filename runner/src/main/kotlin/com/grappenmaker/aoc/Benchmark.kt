package com.grappenmaker.aoc

import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.time.Duration
import kotlin.time.measureTime

fun main(args: Array<String>) {
    fun Puzzle.input() = defaultInput(year, day)
    fun Puzzle.format() = "Year $year Day $day"
    fun Puzzle.run() = SolveContext(this, input().readLines()).implementation()

    val (available, missing) = puzzles.partition { it.input().exists() }
    if (missing.isNotEmpty()) {
        println("Missing inputs (${missing.size}) (skipped):")
        missing.forEach { println("Year ${it.year} Day ${it.day}") }
        println()
    }

    val willRun = if (args.isNotEmpty()) {
        val year = args.first().toInt()
        println("Will only run puzzles in year $year")
        println()

        available.filter { it.year == year }
    } else available

    require(willRun.isNotEmpty()) { "no puzzles, sad" }

    println("\"Warming up\" by running a few random puzzles... if you are unlucky, this can take a while")
    repeat(20) { willRun.random().run() }

    println("Running ${willRun.size}/${puzzles.size} (available) puzzles, this will take a while...")
    println()

    val samples = 10000
    val runningYears = willRun.mapTo(hashSetOf()) { it.year }
    val benches = years.filter { it.year in runningYears }.map { set ->
        set.year to set.puzzles.filter { it in willRun }.map { p ->
            p to measureTime { repeat(samples) { p.run() } } / samples
        }
    }

    println("All results:")
    benches.sortedBy { it.first }.forEach { (set, perPuzzle) ->
        println("- Year $set (${perPuzzle.fold(Duration.ZERO) { acc, (_, t) -> acc + t }})")
        perPuzzle.sortedBy { it.first.day }.forEach { (puzzle, time) -> println("\t- Day ${puzzle.day}: $time") }

        println("Fastest puzzle: Day ${perPuzzle.minBy { it.second }.first.day}")
        println("Slowest puzzle: Day ${perPuzzle.maxBy { it.second }.first.day}")
    }

    val allBenches = benches.flatMap { it.second }
    println("Fastest puzzle: ${allBenches.minBy { it.second }.first.format()}")
    println("Slowest puzzle: ${allBenches.maxBy { it.second }.first.format()}")
}