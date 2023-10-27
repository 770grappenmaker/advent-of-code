package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.splitInts
import kotlin.math.max

fun PuzzleSet.day16() = puzzle {
    val valves = inputLines.map { l ->
        val conns = l.substringAfter("valves ").substringAfter("valve ").split(", ")
        Valve(l.split(" ")[1], l.splitInts().single(), conns)
    }.sortedByDescending { it.rate }

    val initial = valves.indexOfFirst { it.name == "AA" }
    val connections = valves.map { v -> valves.filter { it.name in v.connections }.map(valves::indexOf) }
    val rates = valves.map { it.rate }
    // 134217728 = 2^27
    val seen = MutableList(134217728) { -1 }

    require(valves.size <= 60) { "Input input (too many valves)" }
    require(valves.count { it.rate > 0 } <= 15) { "Invalid input (too many valves output something)" }

    fun solve(valveIndex: Int = initial, opened: Int = 0, timeLeft: Int = 30, partTwo: Boolean = false): Int {
        if (timeLeft <= 0) return if (partTwo) solve(initial, opened, 26, false) else 0

        // valve index - 6 bits -> max 0x3F, shift 27-6=21
        // opened - 15 bits -> max 0x7FFF, shift 21-15=6
        // time left - 5 bits -> max 0x1F, shift 6-5=1
        // part two - 1 bit -> max 0x1, shift 1-1=0
        // total = 6+15+5+1=27 (fits in int)
        val indexed = (valveIndex and 0x3F shl 21) or (opened and 0x7FFF shl 6) or
                (timeLeft and 0x1F shl 1) or (if (partTwo) 1 else 0)

        val seenValue = seen[indexed]
        if (seenValue != -1) return seenValue

        var result = 0
        connections[valveIndex].forEach { new -> result = max(solve(new, opened, timeLeft - 1, partTwo), result) }

        val shift = 1 shl valveIndex
        val rate = rates[valveIndex]
        if (rate > 0 && opened and shift == 0) {
            result = max(solve(valveIndex, opened or shift, timeLeft - 1, partTwo) + (timeLeft - 1) * rate, result)
        }

        seen[indexed] = result
        return result
    }

    partOne = solve(initial).s()
    partTwo = solve(initial, timeLeft = 26, partTwo = true).s()
}

data class Valve(val name: String, val rate: Int, val connections: List<String>)