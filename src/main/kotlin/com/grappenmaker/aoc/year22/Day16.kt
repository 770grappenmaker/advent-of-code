package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.max

fun PuzzleSet.day16() = puzzle {
    val valves = inputLines.map { l ->
        val conns = l.substringAfter("valves ").split(", ")
        Valve(l.split(" ")[1], l.splitInts().single(), conns)
    }

    val connections = valves.associateWith { v -> valves.filter { it.name in v.connections } }
    val initial = valves.first { it.name == "AA" }
    val seen = hashSetOf<SearchThing>()

    fun solve(curr: SearchThing): Int {
        if (curr.timeLeft <= 0 || !seen.add(curr)) return 0

        var result = 0
        val openSum = curr.openSum

        if (curr.valve.rate > 0 && curr.valve !in curr.opened) {
            result = max(
                openSum + solve(
                    curr.copy(
                        opened = curr.opened + curr.valve,
                        timeLeft = curr.timeLeft - 1
                    )
                ), result
            )
        }

        connections.getValue(curr.valve).forEach { new ->
            result = max(
                openSum + solve(
                    curr.copy(
                        valve = new,
                        timeLeft = curr.timeLeft - 1
                    )
                ), result
            )
        }

        return result
    }

    partOne = solve(SearchThing(initial)).s()
}

data class SearchThing(val valve: Valve, val opened: Set<Valve> = setOf(), val timeLeft: Int = 30)

val SearchThing.openSum get() = opened.sumOf { it.rate }

data class Valve(val name: String, val rate: Int, val connections: List<String>)