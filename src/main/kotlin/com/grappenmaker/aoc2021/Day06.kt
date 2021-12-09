package com.grappenmaker.aoc2021

fun Solution.solveDay6() {
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

    val partOne = cyclePopulation(fish, 80)
    println("Part one: ${partOne.values.sum()}")

    // Part two
    println("Part two: ${cyclePopulation(partOne, 256 - 80).values.sum()}")
}