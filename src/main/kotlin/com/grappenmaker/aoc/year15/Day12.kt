package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import kotlinx.serialization.json.*

fun PuzzleSet.day12() = puzzle {
    // Nice solution, but this will not do for part two
    // val numberRegex = "-?\\d+".toRegex()
    // partOne = numberRegex.findAll(input).sumOf { it.value.toInt() }.s()

    val root = Json.parseToJsonElement(input)
    partOne = root.walk().s()
    partTwo = root.walk {
        val values = it.values.filterIsInstance<JsonPrimitive>().mapNotNull { p -> p.contentOrNull }

        // Damn it, elves!
        "red" !in it && "red" !in values
    }.s()
}

fun JsonElement.walk(condition: (JsonObject) -> Boolean = { true }): Int = when(this) {
    is JsonPrimitive -> intOrNull ?: 0
    is JsonArray -> sumOf { it.walk(condition) }
    is JsonObject -> if (condition(this)) values.sumOf { it.walk(condition) } else 0
}