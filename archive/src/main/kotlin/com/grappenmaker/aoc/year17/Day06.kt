package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.firstNotDistinctBy
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day6() = puzzle {
    fun solve(start: List<Int>) = generateSequence(start) { old ->
        val new = old.toMutableList()
        val (toRemove, value) = new.withIndex().maxBy { it.value }
        new[toRemove] = 0
        repeat(value) { i -> new[(toRemove + i + 1) % new.size]++ }

        new
    }.withIndex().firstNotDistinctBy { it.value }

    val (idx, partOneResult) = solve(input.splitInts())
    partOne = idx.toString()
    partTwo = solve(partOneResult).index.toString()
}

// I thought this was an optimization, but it was not
//        new.mapInPlaceIndexed { idx, v ->
//            val distance = (idx - toRemove - 1).mod(new.size)
//            v + ((value - distance - 1) / new.size).coerceAtLeast(0) + if (distance < value) 1 else 0
//        }