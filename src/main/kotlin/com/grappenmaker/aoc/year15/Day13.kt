package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day13() = puzzle {
    val exclusiveSeatings = inputLines.map { l ->
        val split = l.split(" ")
        val type = when (split[2]) {
            "gain" -> 1
            "lose" -> -1
            else -> error("What?")
        }

        val units = split[3].toInt() * type
        val nextTo = split.last().removeSuffix(".")
        SeatingOption(split[0], nextTo, units)
    }

    fun solve(seatings: List<SeatingOption>, people: List<String>): String {
        val findFor = { person: String, nextTo: String ->
            seatings.find { it.person == person && it.nextTo == nextTo }?.points ?: 0
        }

        return people.permutations().maxOf { configuration ->
            configuration.withIndex().sumOf { (idx, p) ->
                val left = configuration.getRolledOver(idx - 1)
                val right = configuration.getRolledOver(idx + 1)
                findFor(p, left) + findFor(p, right)
            }
        }.s()
    }

    val people = exclusiveSeatings.map { it.person }.distinct()
    partOne = solve(exclusiveSeatings, people)
    partTwo = solve(exclusiveSeatings, people + "Me")
}

fun <T> List<T>.getRolledOver(index: Int): T = when {
    index >= size -> this[index % size]
    index < 0 -> getRolledOver(size + index)
    else -> this[index]
}

data class SeatingOption(val person: String, val nextTo: String, val points: Int)