package com.grappenmaker.aoc.practice24.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.frequencies

fun PuzzleSet.day06() = puzzle(day = 6) {
    var state = input.split(",").map { it.toInt() }.frequencies().mapValues { it.value.toLong() }

    fun simul(d: Int) = repeat(d) {
        state = buildMap {
            fun add(k: Int, v: Long) = put(k, v + getOrDefault(k, 0))

            for ((k, v) in state) {
                if (k != 0) {
                    add(k - 1, v)
                    continue
                }

                add(6, v)
                add(8, v)
            }
        }
    }

    simul(80)
    partOne = state.values.sum()

    simul(256 - 80)
    partTwo = state.values.sum()
}