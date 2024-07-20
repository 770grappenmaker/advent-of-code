package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.permutations

@PuzzleEntry
fun PuzzleSet.day9() = puzzle {
    val edges = inputLines.map { l ->
        val (from, _, to, _, distance) = l.split(" ")
        Route(from, to, distance.toInt())
    }

    // I'm sure this can be done better
    val vertices = edges.flatMap { listOf(it.from, it.to) }.distinct()

    // Idea: take the permutations of all vertices and find the best path
    // Original idea: use dijkstra, somehow
    val distances = vertices.permutations().map { perm ->
        perm.dropLast(1).withIndex().sumOf { (idx, vertex) ->
            val next = perm[idx + 1]
            edges.first { (from, to) ->
                (from == vertex && to == next) || (from == next && to == vertex)
            }.distance
        }
    }

    partOne = distances.min().s()
    partTwo = distances.max().s()
}

// basically an edge
data class Route(val from: String, val to: String, val distance: Int)