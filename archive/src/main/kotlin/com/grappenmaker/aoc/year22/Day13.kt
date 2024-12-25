package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asList
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlinx.serialization.json.*
import kotlin.math.min

@PuzzleEntry
fun PuzzleSet.day13() = puzzle {
    val pairs = input.split("\n\n").map { p -> p.lines().map { parseValue(it) }.asPair() }
    partOne = pairs.mapIndexed { idx, (a, b) -> a.inOrderWith(b) to idx + 1 }
        .filter { (a) -> a == 1 }.sumOf { (_, idx) -> idx }.toString()

    val flat = pairs.flatMap { it.asList() }
    val div1 = ValueList(listOf(ValueList(listOf(LiteralValue(2)))))
    val div2 = ValueList(listOf(ValueList(listOf(LiteralValue(6)))))
    val sorted = (flat + div1 + div2).sortedDescending()
    partTwo = ((sorted.indexOf(div1) + 1) * (sorted.indexOf(div2) + 1)).toString()
}

// Coding under pressure is not fun
fun JsonArray.parse(): ValueList = ValueList(
    map {
        when (it) {
            is JsonPrimitive -> LiteralValue(it.int)
            is JsonArray -> it.parse()
            else -> error("Invalid value")
        }
    }
)

fun parseValue(str: String) = Json.parseToJsonElement(str).jsonArray.parse()

sealed interface Value : Comparable<Value> {
    override operator fun compareTo(other: Value): Int = when {
        this is LiteralValue && other is LiteralValue -> -value.compareTo(other.value)
        this is ValueList && other is ValueList -> inOrderWith(other)
        else -> toList().compareTo(other.toList())
    }
}

@JvmInline
value class ValueList(val values: List<Value>) : Value

fun ValueList.inOrderWith(other: ValueList): Int {
    values.zip(other.values).forEach { (a, b) ->
        val cmp = a.compareTo(b)
        if (cmp != 0) return cmp
    }

    val mSize = min(values.size, other.values.size)
    return when {
        mSize == values.size && mSize < other.values.size -> 1
        mSize == other.values.size && mSize < values.size -> -1
        else -> 0
    }
}

fun Value.toList() = when (this) {
    is ValueList -> this
    is LiteralValue -> ValueList(listOf(this))
}

@JvmInline
value class LiteralValue(val value: Int) : Value