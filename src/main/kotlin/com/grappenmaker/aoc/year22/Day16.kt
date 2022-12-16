package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.max

fun PuzzleSet.day16() = puzzle {
    val valves = inputLines.map { l ->
        val conns = l.substringAfter("valves ").substringAfter("valve ").split(", ")
        Valve(l.split(" ")[1], l.splitInts().single(), conns)
    }

    val connections = valves.associateWith { v -> valves.filter { it.name in v.connections } }
    val initial = valves.first { it.name == "AA" }
    val seen = hashMapOf<ValveState, Int>()

    fun solve(curr: ValveState): Int {
        if (curr.timeLeft <= 0) return if (curr.partTwo) solve(
            curr.copy(
                valve = initial,
                timeLeft = 26,
                partTwo = false
            )
        ) else 0

        seen[curr]?.let { return it }

        var result = 0

        connections.getValue(curr.valve).forEach { new ->
            result = max(
                solve(
                    curr.copy(
                        valve = new,
                        timeLeft = curr.timeLeft - 1
                    )
                ), result
            )
        }

        if (curr.valve.rate > 0 && curr.valve !in curr.opened) {
            result = max(
                solve(
                    curr.copy(
                        opened = curr.opened + curr.valve,
                        timeLeft = curr.timeLeft - 1
                    )
                ) + (curr.timeLeft - 1) * curr.valve.rate, result
            )
        }

        seen[curr] = result
        return result
    }

    partOne = solve(ValveState(initial)).s()
    partTwo = solve(ValveState(initial, timeLeft = 26, partTwo = true)).s()
}

data class ValveState(
    val valve: Valve,
    val timeLeft: Int = 30,
    val opened: Set<Valve> = setOf(),
    val partTwo: Boolean = false
)

data class Valve(val name: String, val rate: Int, val connections: List<String>)