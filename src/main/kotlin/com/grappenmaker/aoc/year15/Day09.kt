package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import java.util.Collections.swap

fun PuzzleSet.day9() = puzzle {
    val edges = inputLines.map { l ->
        val (from, _, to, _, distance) = l.split(" ")
        Route(from, to, distance.toInt())
    }

    // I'm sure this can be done better
    val vertices = edges.flatMap { listOf(it.from, it.to) }.toSet().toList()

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

// Heap's algorithm
// See https://en.wikipedia.org/wiki/Heap%27s_algorithm
fun <T> List<T>.permutations(): List<List<T>> = buildList {
    fun recurse(list: List<T>, k: Int) {
        if (k == 1) {
            add(list.toList())
            return
        }

        for (i in 0 until k) {
            recurse(list, k - 1)
            swap(list, if (k % 2 == 0) i else 0, k - 1)
        }
    }

    recurse(this@permutations.toList(), this@permutations.size)
}

// basically an edge
data class Route(val from: String, val to: String, val distance: Int)