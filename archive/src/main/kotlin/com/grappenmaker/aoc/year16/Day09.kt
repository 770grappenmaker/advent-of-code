package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.onceSplit

@PuzzleEntry
fun PuzzleSet.day09() = puzzle {
    fun String.createParts(): List<ExplodePart> = buildList {
        var left = this@createParts
        while (left.isNotEmpty()) {
            if ("(" !in left) {
                add(LengthPart(left.length))
                break
            }

            val (before, after) = left.onceSplit("(", "")
            if (before.isNotEmpty()) add(LengthPart(before.length))

            val (marker, rest) = after.onceSplit(")", "")
            left = rest

            if (after.isEmpty()) break
            if (marker.isNotEmpty()) {
                val (x, y) = marker.split("x").map(String::toInt)
                add(MarkerPart(x, y, left.take(x).createParts()))

                left = left.drop(x)
            }
        }
    }

    val parts = input.createParts()

    partOne = parts.sumOf {
        when (it) {
            is LengthPart -> it.length
            is MarkerPart -> it.amount * it.times
        }
    }.s()

    fun List<ExplodePart>.partTwo(): Long = sumOf {
        when (it) {
            is LengthPart -> it.length.toLong()
            is MarkerPart -> it.times.toLong() * it.parts.partTwo()
        }
    }

    partTwo = parts.partTwo().s()
}

sealed interface ExplodePart
data class LengthPart(val length: Int) : ExplodePart
data class MarkerPart(val amount: Int, val times: Int, val parts: List<ExplodePart>) : ExplodePart