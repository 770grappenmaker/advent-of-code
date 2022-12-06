package com.grappenmaker.aoc.year21

import java.util.Collections.swap

fun <T> List<T>.permutations(): List<List<T>> {
    val result = mutableListOf<List<T>>()

    // Uses heaps algorithm
    fun generate(list: List<T>, k: Int) {
        if (k == 1) {
            result.add(list.toList())
            return
        }

        for (i in 0 until k) {
            // Goes deeper into the recursion
            generate(list, k - 1)

            // Checks for parity
            if (k % 2 == 0) {
                // Swaps out stuff (read wiki on the algo)
                swap(list, i, k - 1)
            } else {
                swap(list, 0, k - 1)
            }
        }
    }

    generate(toList(), size)
    return result
}

fun <T> List<T>.permutationPairs(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    forEach { a -> this.mapTo(result) { a to it } }
    return result
}