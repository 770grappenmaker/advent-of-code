package com.grappenmaker.aoc2021

import java.util.*

fun Solution.solveDay15(): Answer {
    // Get input
    val width = inputLines.first().length
    val height = inputLines.size
    val graph = inputLines.flatMap { line -> line.map { it.code - 48 } }

    // Part one and two
    return solve(graph, width, height) to 0
}

fun solve(graph: List<Int>, width: Int, height: Int, blocks: Int = 1): Int {
    // Let's perform dijkstra, initialize queue
    val totalSize = graph.size * blocks * blocks
    val queue = ArrayDeque((0 until totalSize).toSet())

    // Create the risk levels and previous nodes variables
    val levels = mutableMapOf(0 to graph.first()) // index to risk level, add source
    val prev = mutableMapOf<Int, Int>() // Destination to source indexes
    val target = totalSize - 1 // Index of last element is the target

    val getValue: List<Int>.(Int) -> Int = { idx ->
        if (blocks == 1) this[idx] else {
            val offset = asPoint(idx / blocks, blocks).let { it.x + it.y }
            var result = this[idx % this.size] + offset
            while (result > 9) result -= 9
            result
        }
    }

    // Perform it
    while (queue.isNotEmpty()) {
        // Get minimal tentative risk level
        val idx = queue.minByOrNull { levels[it] ?: Integer.MAX_VALUE }!!
        println(idx)

        // Pop from queue
        queue.remove(idx)

        // Check if we have found a result
        if (prev[target] != null || idx == target) {
            var result = 0
            var current: Int? = target

            while (current != null) {
//                result += graph[current]
                result += graph.getValue(current)
                current = prev[current]
            }

            return result - graph.first()
        }

        // Check for all adjacents
        for (adj in getAdjacentsStraight(asPoint(idx, width), width, height)) {
//            val len = levels[idx]!! + graph[adj]
            val len = levels[idx]!! + graph.getValue(adj)
            if (len < (levels[adj] ?: Integer.MAX_VALUE)) {
                levels[adj] = len
                prev[adj] = idx
            }
        }
    }

    error("Impossible")
}