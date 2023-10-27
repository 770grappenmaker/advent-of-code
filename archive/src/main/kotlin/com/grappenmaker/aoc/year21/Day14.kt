package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day14() = puzzle(day = 14) {
    // Get the input
    val (template, rules) = input.split("\n\n")

    // Window (or count) pairs
    val polymer = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    val mapping = rules.split("\n").map { line ->
        val split = line.split(" -> ")
        split.first() to split.last()
    }

    // Perform a step, as in apply mappings and return the new map
    val step = { current: Map<String, Long> ->
        val result = mutableMapOf<String, Long>()
        current.forEach { (part, count) ->
            mapping.find { (find, _) -> find == part }?.let { (_, replace) ->
                result[part.first() + replace] = (result[part.first() + replace] ?: 0) + count
                result[replace + part.last()] = (result[replace + part.last()] ?: 0) + count
            }
        }

        result
    }

    // Get the result after an arbitrary number of steps
    val getResultAfter = { start: Map<String, Long>, steps: Int -> (1..steps).fold(start) { cur, _ -> step(cur) } }

    // Calculate the puzzle answer (both parts)
    val calculate = { values: Map<String, Long> ->
        val result = values.entries.fold(mutableMapOf<Char, Long>()) { cur, entry ->
            cur[entry.key.first()] = (cur[entry.key.first()] ?: 0L) + entry.value
            cur
        }.mapValues { (k, v) -> if (k == template.last()) v + 1 else v }

        result.maxOf { it.value } - result.minOf { it.value }
    }

    // Return the result
    val partOneResult = getResultAfter(polymer, 10)
    partOne = calculate(partOneResult).s()
    partTwo = calculate(getResultAfter(partOneResult, 30)).s()
}