package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year15.AuntProperty.*

val givenInfo = mapOf(
    CHILDREN to 3,
    CATS to 7,
    SAMOYEDS to 2,
    POMERANIANS to 3,
    AKITAS to 0,
    VIZSLAS to 0,
    GOLDFISH to 5,
    TREES to 3,
    CARS to 2,
    PERFUMES to 1
)

fun PuzzleSet.day16() = puzzle {
    val aunts = inputLines.map { l ->
        val props = l.substringAfter(": ").split(", ")
        props.associate {
            val (key, value) = it.split(": ")
            auntPropByName(key) to value.toInt()
        }
    }

    fun solve(partTwo: Boolean) = (aunts.indexOfFirst { aunt ->
        aunt.all { (prop, value) ->
            val givenValue = givenInfo.getValue(prop)

            if (partTwo) when(prop) {
                CATS, TREES -> givenValue < value
                POMERANIANS, GOLDFISH -> givenValue > value
                else -> givenValue == value
            } else givenValue == value
        }
    } + 1)

    partOne = solve(false).s()
    partTwo = solve(true).s()
}

// Is this over-engineering? maybe
enum class AuntProperty {
    CHILDREN, CATS,
    SAMOYEDS, POMERANIANS, AKITAS, VIZSLAS,
    GOLDFISH, TREES, CARS, PERFUMES;

    val propName get() = name.lowercase()
}

fun auntPropByName(name: String) = enumValues<AuntProperty>().first { it.propName == name }