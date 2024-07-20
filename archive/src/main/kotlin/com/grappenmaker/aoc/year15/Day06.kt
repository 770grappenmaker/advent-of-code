package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day6() = puzzle {
    val toggled = mutableBooleanGrid(1000, 1000)
    val brightness = mutableIntGrid(1000, 1000)

    inputLines.forEach { l ->
        val (a, b) = l.trim().split(" ").filter { "," in it }
            .map { it.split(",").map(String::toInt).asPair().toPoint() }

        Rectangle(a, b).points.forEach {
            val (toggleValue, deltaBrightness) = when {
                l.startsWith("turn on") -> true to 1
                l.startsWith("turn off") -> false to -1
                l.startsWith("toggle") -> !toggled[it] to 2
                else -> error("Impossible")
            }

            toggled[it] = toggleValue
            brightness[it] = maxOf(0, brightness[it] + deltaBrightness)
        }
    }

    partOne = toggled.countTrue().s()
    partTwo = brightness.sum().s()
}