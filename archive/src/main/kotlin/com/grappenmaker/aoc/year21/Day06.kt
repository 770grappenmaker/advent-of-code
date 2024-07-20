package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day6() = puzzle(day = 6) {
    // Part one
    val fish = input.split(",").groupingBy { it.trim().toInt() }.eachCount()
        .mapValues { it.value.toLong() }

    val cyclePopulation = { map: Map<Int, Long>, count: Int ->
        var result = map

        for (day in 1..count) {
            val newResult = mutableMapOf<Int, Long>()

            for ((i, cnt) in result) {
                if (i == 0) {
                    newResult[6] = newResult.getOrDefault(6, 0) + cnt
                    newResult[8] = newResult.getOrDefault(8, 0) + cnt
                } else {
                    val newKey = i - 1
                    newResult[newKey] = newResult.getOrDefault(newKey, 0) + cnt
                }
            }

            result = newResult
        }

        result
    }

    val popPartOne = cyclePopulation(fish, 80)
    partOne = popPartOne.values.sum().s()

    // Part two
    partTwo = cyclePopulation(popPartOne, 256 - 80).values.sum().s()
}
