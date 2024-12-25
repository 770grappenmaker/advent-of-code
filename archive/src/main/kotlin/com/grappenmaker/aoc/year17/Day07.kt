package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.diff
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.absoluteValue

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(day = 7) {
    val tree = inputLines.map { l ->
        val parts = l.split(" -> ")
        val (name, weightPart) = parts.first().split(" ")
        val weight = weightPart.drop(1).dropLast(1).toInt()
        val children = parts.getOrNull(1)?.split(", ")?.toSet() ?: emptySet()
        DiscEntry(name, weight, children)
    }

    fun DiscEntry.parent() = (tree - this).singleOrNull { name in it.children }
    val head = tree.single { it.parent() == null }
    partOne = head.name

    val byName = tree.associateBy { it.name }
    val weights = mutableMapOf<String, Int>()

    fun DiscEntry.weight(): Int = weights.getOrPut(name) { weight + children.sumOf { byName.getValue(it).weight() } }
    fun DiscEntry.foundChildren() = children.map { byName.getValue(it) }
    fun DiscEntry.balanced() = foundChildren().map { it.weight() }.distinct().singleOrNull() != null
    fun DiscEntry.find(offset: Int? = null): Int = when {
        offset != null && balanced() -> weight - offset
        else -> {
            val toSearch = foundChildren()
            val wrong = toSearch.single { e ->
                val other = toSearch - e
                val target = (other.firstOrNull() ?: e).weight()
                other.all { it.weight() == target } && e.weight != target
            }

            wrong.find(
                offset ?: toSearch.map { it.weight() }.toSet()
                    .also { assert(it.size == 2) }.diff().absoluteValue
            )
        }
    }

    partTwo = head.find().toString()
}

private data class DiscEntry(val name: String, val weight: Int, val children: Set<String>)