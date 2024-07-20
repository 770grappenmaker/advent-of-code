package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day14() = puzzle {
    val allReindeer = inputLines.map { l ->
        val split = l.split(" ")
        Reindeer(split[0], split[3].toInt(), split[6].toInt(), split[13].toInt())
    }

    val timeRange = 0..<2503 // seconds
    val travelMap = allReindeer.associateWith { reindeer ->
        timeRange.scan(0) { acc, curr -> acc + if (reindeer.isResting(curr)) 0 else reindeer.speed }
    }.toList()

    partOne = travelMap.maxOf { (_, travels) -> travels.last() }.s()

    val leads = timeRange.map { curr ->
        val sorted = travelMap.map { (reindeer, travels) -> reindeer to travels[curr + 1] }
            .sortedByDescending { (_, d) -> d }

        val maxDistance = sorted.first().second
        sorted.takeWhile { (_, d) -> d == maxDistance }.map { (r) -> r }
    }

    partTwo = travelMap.maxOf { (reindeer) -> leads.count { reindeer in it } }.s()
}

data class Reindeer(
    val name: String,
    // km/s
    val speed: Int,
    // s
    val flightDuration: Int,
    // s
    val restTime: Int
)

val Reindeer.period get() = flightDuration + restTime

fun Reindeer.isResting(time: Int) = time % period >= flightDuration