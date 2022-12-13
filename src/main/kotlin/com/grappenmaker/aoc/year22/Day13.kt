package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.ComparisonResult.*
import kotlinx.serialization.json.*
import kotlin.math.min

fun PuzzleSet.day13() = puzzle {
    val pairs = input.split("\n\n").map { p -> p.lines().map { parseValue(it) }.asPair() }
    partOne = pairs.mapIndexed { idx, (a, b) -> a.inOrderWith(b) to idx + 1 }
        .filter { (a) -> a == YES }.sumOf { (_, idx) -> idx }.s()

    val flat = pairs.flatMap { it.asList() }
    val div1 = ValueList(listOf(ValueList(listOf(LiteralValue(2)))))
    val div2 = ValueList(listOf(ValueList(listOf(LiteralValue(6)))))
    val sorted = (flat + div1 + div2).sortedWith { a, b ->
        when (a.compare(b)) {
            YES -> -1
            NO -> 1
            UNDECIDED -> 0
        }
    }

    partTwo = ((sorted.indexOf(div1) + 1) * (sorted.indexOf(div2) + 1)).s()
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

sealed interface Value

@JvmInline
value class ValueList(val values: List<Value>) : Value

fun ValueList.inOrderWith(other: ValueList): ComparisonResult {
    values.zip(other.values).forEach { (a, b) ->
        val cmp = a.compare(b)
        if (cmp != UNDECIDED) return cmp
    }

    val mSize = min(values.size, other.values.size)
    return when {
        mSize == values.size && mSize < other.values.size -> YES
        mSize == other.values.size && mSize < values.size -> NO
        else -> UNDECIDED
    }
}

fun Value.compare(other: Value): ComparisonResult = when {
    this is LiteralValue && other is LiteralValue -> when {
        value < other.value -> YES
        value == other.value -> UNDECIDED
        else -> NO
    }

    this is ValueList && other is ValueList -> inOrderWith(other)
    this is ValueList -> compare(ValueList(listOf(other)))
    else -> toList().compare(other.toList())
}

fun Value.toList() = when (this) {
    is ValueList -> this
    is LiteralValue -> ValueList(listOf(this))
}

@JvmInline
value class LiteralValue(val value: Int) : Value

enum class ComparisonResult {
    YES, UNDECIDED, NO
}