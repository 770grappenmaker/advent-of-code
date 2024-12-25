package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(day = 12) {
    // Part one
    val graph = inputLines.generateGraph()

    // Initialize queue for BFS
    val queue = ArrayDeque(listOf(Node("start")))

    // Initialize counters
    var result = 0
    var additional = 0

    // Perform BFS
    while (queue.isNotEmpty()) {
        // Pop first element, check if it is the destination (end)
        val (name, visitedSmall, twice) = queue.removeFirst()
        if (name == "end") {
            if (twice == null) result++ else additional++
            continue
        }

        // Go through all connected nodes
        // and enqueue them if:
        // 1. It is not "start"
        // 2. For part one, it should not be small
        // 3. For part two, it can be small at most twice
        graph[name]?.forEach {
            if (it == "start") return@forEach
            if (it !in visitedSmall) {
                queue.addLast(Node(it, buildSet {
                    addAll(visitedSmall)
                    if (it.isLowerCase()) add(it)
                }, twice))
            } else if (twice == null && it != "end") {
                // Part two
                // Checking for "end" here, because the node may contain
                // "end" when it is not the twice visited cave,
                // but it can't be end here, because then it wouldn't be
                // visited twice
                queue.addLast(Node(it, visitedSmall, it))
            }
        }
    }

    // Return the results of the search
    partOne = result.toString()
    partTwo = (result + additional).toString()
}

// Utility to check if full string is lowercase
fun String.isLowerCase() = all { it.isLowerCase() }

// Data class to keep track of all visited elements, and the current element
// It's called node because it is a node in the queue (elements in BFS are called nodes)
private data class Node(val name: String, val visitedSmall: Set<String> = setOf(), var visitedSmallTwice: String? = null)

// Generate graph (maths and cs data structure)
fun List<String>.generateGraph(): Map<String, List<String>> {
    // 1. Maps everything to a List<Pair<String, String>>,
    // which represents a list of key-value and value-key pairs,
    // which then represent a vertex
    // 2. Groups by key
    // 3. Maps to value
    // Result: Map of node to possible destination nodes
    // or: a map of vertexes
    return flatMap {
        val split = it.split("-")
        listOf(split[0] to split[1], split[1] to split[0])
    }.groupBy { it.first }.mapValues { it.value.map { v -> v.second } }
}