package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.pow

// Maps segments count to the number (we do be lazy)
val knownLengths = mapOf(1 to 2, 7 to 3, 4 to 4, 8 to 7)

@PuzzleEntry
fun PuzzleSet.day8() = puzzle(day = 8) {
    // Part one
    val data = inputLines.map { it.split(" | ") }

    // All lengths we know
    partOne = data.sumOf { it[1].split(" ").count { e -> e.length in knownLengths.values } }.s()

    // Part two
    partTwo = data.map { line ->
        // 97 isn't magic: it's equivalent to 'a'.code
        val remap: (String) -> Set<Int> = { seg -> seg.map { it.code - 97 }.toSet() }
        line.first().split(" ").map(remap) to line[1].split(" ").map(remap)
    }.sumOf {
        val mapping = getMapping(it.first)
        it.second.foldIndexed(0) { idx, acc, value ->
            acc + mapping.getValue(value) * 10.0.pow((it.second.size - idx - 1).toDouble()).toInt()
        }.toInt()
    }.s()
}

// Lot of magic going on here
private fun getMapping(segments: List<Set<Int>>): Map<Set<Int>, Int> {
    // Initialize mapping
    val mapping = mutableMapOf<Int, Set<Int>>()

    // Get the ones we "know"
    // Again, lazy... or smart??
    knownLengths.forEach { (k, v) -> mapping[k] = segments.first { it.size == v } }

    // Util for convenience
    val mapDigit = { digit: Int, size: Int, condition: (Set<Int>) -> Boolean ->
        mapping[digit] = segments.first { it.size == size && condition(it) }
    }

    // Map digit 3 (has size of 5 and should overlap with 1)
    mapDigit(3, 5) { it overlays mapping.getValue(1) }
    // Map digit 9 (has size of 6 and should overlap with 3)
    mapDigit(9, 6) { it overlays mapping.getValue(3) }
    // Map digit 0 (has size of 6 and should overlap with 1 and 7, and is not nine)
    mapDigit(0, 6) { it overlays mapping.getValue(1) && it overlays mapping.getValue(7) && it != mapping[9] }
    // Map digit 6 (has size of 6 and should NOT overlay with 0 or 9 at all)
    mapDigit(6, 6) { it != mapping[0] && it != mapping[9] }
    // Map digit 5 (has size of 5 and 6 should overlap with it)
    mapDigit(5, 5) { mapping.getValue(6) overlays it }
    // Map digit 2 (has size of 5 and should NOT overlay with 3 or 5 at all)
    mapDigit(2, 5) { it != mapping[3] && it != mapping[5] }

    // Invert the mapping, again because im a lazy f*ck and ive spent way
    // too much time on this puzzle.
    return mapping.map { it.value to it.key }.toMap()
}

// Probably a little bit cleaner
private infix fun <T> Set<T>.overlays(other: Set<T>) = this.containsAll(other)