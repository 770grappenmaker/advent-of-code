package com.grappenmaker.aoc

import com.grappenmaker.aoc.years.puzzles
import com.grappenmaker.aoc.years.years
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

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
    repeat(5) { willRun.random().run() }

    println("Running ${willRun.size}/${puzzles.size} (available) puzzles, this will take a while...")
    println()

    val runningYears = willRun.mapTo(hashSetOf()) { it.year }
    val benches = years.filter { it.year in runningYears }.map { set ->
        set.year to set.puzzles.filter { it in willRun }.map { it to measureTimeMillis { it.run() } }
    }

    println("All results:")
    benches.sortedBy { it.first }.forEach { (set, perPuzzle) ->
        println("- Year $set (${perPuzzle.sumOf { (_, t) -> t }}ms)")
        perPuzzle.sortedBy { it.first.day }.forEach { (puzzle, time) -> println("\t- Day ${puzzle.day}: ${time}ms") }

        println("Fastest puzzle: Day ${perPuzzle.minBy { it.second }.first.day}")
        println("Slowest puzzle: Day ${perPuzzle.maxBy { it.second }.first.day}")
    }

    println()
    println(
        "Fastest year (based on median): ${
            benches.minBy { (_, perPuzzle) ->
                val sorted = perPuzzle.map { it.second }.sorted()
                sorted[sorted.size / 2]
            }.first
        }"
    )

    println("Fastest year (based on mean): ${benches.minBy { (_, p) -> p.sumOf { it.second } / p.size }.first}")

    val allBenches = benches.flatMap { it.second }
    println("Fastest puzzle: ${allBenches.minBy { it.second }.first.format()}")
    println("Slowest puzzle: ${allBenches.maxBy { it.second }.first.format()}")
}