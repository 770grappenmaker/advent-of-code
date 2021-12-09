package com.grappenmaker.aoc2021

import kotlin.math.floor

fun Solution.solveDay9() {
    val width = inputLines.first().length
    val height = inputLines.size

    // 48 isn't magic: it is my wacky way to parse numbers
    // (48 is '0'.code)
    val heightmap = inputLines.flatMap { line -> line.map { it.code - 48 } }

    val getAdjacentsIndexes = { x: Int, y: Int ->
        arrayOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
            .mapNotNull {
                val newX = x + it.first
                val newY = y + it.second
                if (newX !in 0 until width || newY !in 0 until height) return@mapNotNull null

                val index = asIndex(newX, newY, width)
                if (index !in heightmap.indices) null else index
            }
    }

    // Part one
    val lowPoints = heightmap.mapIndexedNotNull { idx, i ->
        val (x, y) = asXY(idx, width)
        if (getAdjacentsIndexes(x, y).all { i < heightmap[it] }) {
            idx
        } else null
    }

    val partOne = lowPoints.sumOf { heightmap[it] + 1 }
    println("Part one: $partOne")

    // Part two
    val basins = lowPoints.map { num ->
        var basinSize = 0
        val queue = ArrayDeque(listOf(num))
        val seenIndexes = mutableSetOf<Int>()

        while (queue.isNotEmpty()) {
            val newIndex = queue.removeFirst()
            if (!seenIndexes.add(newIndex)) continue
            basinSize += 1

            val (x, y) = asXY(newIndex, width)
            val newAdjacents = getAdjacentsIndexes(x, y)
                .filter { heightmap[it] != 9 && !seenIndexes.contains(it) }
            queue.addAll(newAdjacents)
        }

        basinSize
    }.sortedDescending()

    println("Part two: ${basins[0] * basins[1] * basins[2]}")
}

// Util
fun asIndex(x: Int, y: Int, width: Int) = x + y * width
fun asXY(idx: Int, width: Int) = idx % width to floor(idx.toDouble() / width.toDouble()).toInt()