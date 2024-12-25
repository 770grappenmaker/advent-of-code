@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day11() = puzzle(day = 11) {
    val initial = input.split(' ').map { it.toLong() }.frequencies().mapValues { it.value.toLong() }

    fun Map<Long, Long>.update(): Map<Long, Long> = buildMap {
        fun add(key: Long, value: Long) {
            this[key] = getOrDefault(key, 0L) + value
        }

        for ((k, v) in this@update) {
            val s = k.toString()

            when {
                k == 0L -> add(1, v)
                s.length % 2 == 0 -> {
                    val (a, b) = s.chunked(s.length / 2)
                    add(a.toLong(), v)
                    add(b.toLong(), v)
                }

                else -> add(k * 2024L, v)
            }
        }
    }

    fun solve(n: Int) = generateSequence(initial) { it.update() }.nth(n).values.sum()
    partOne = solve(25)
    partTwo = solve(75)
}