package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day20() = puzzle {
    val encrypted = inputLines.map(String::toLong)
    fun List<Long>.step(rounds: Int = 1): List<Long> {
        val sizem1 = size - 1
        val mut = withIndex().toMutableList()
        repeat(rounds) {
            forEachIndexed { idx, v ->
                val mutIdx = mut.indexOfFirst { it.index == idx }
                val mv = mut[mutIdx]

                mut.removeAt(mutIdx)
                mut.add((mutIdx + v).mod(sizem1), mv)
            }
        }

        return mut.map { it.value }
    }

    fun <T> List<T>.nth(n: Int) = this[n % size]
    fun List<Long>.coords(): Long {
        val zeroIndex = indexOfFirst { it == 0L }
        return nth(1000 + zeroIndex) + nth(2000 + zeroIndex) + nth(3000 + zeroIndex)
    }

    partOne = encrypted.step().coords().s()
    partTwo = encrypted.map { it * 811589153L }.step(10).coords().s()
}