package com.grappenmaker.aoc2021

fun Solution.solveDay9(): Pair<Int, Int> {
    val width = inputLines.first().length
    val height = inputLines.size

    // 48 isn't magic: it is my wacky way to parse numbers
    // (48 is '0'.code)
    val heightmap = inputLines.flatMap { line -> line.map { it.code - 48 } }

    val getAdjacentsIndexes =
        { point: Point -> getAdjacentsStraight(point, width, height).filter { it in heightmap.indices } }

    // Part one
    val lowPoints = heightmap.mapIndexedNotNull { idx, i ->
        if (getAdjacentsIndexes(asXY(idx, width)).all { i < heightmap[it] }) {
            idx
        } else null
    }

    val partOne = lowPoints.sumOf { heightmap[it] + 1 }

    // Part two
    val basins = lowPoints.map { num ->
        var basinSize = 0
        val queue = ArrayDeque(listOf(num))
        val seenIndexes = mutableSetOf<Int>()

        while (queue.isNotEmpty()) {
            val newIndex = queue.removeFirst()
            if (!seenIndexes.add(newIndex)) continue
            basinSize += 1

            val newAdjacents = getAdjacentsIndexes(asXY(newIndex, width))
                .filter { heightmap[it] != 9 && !seenIndexes.contains(it) }
            queue.addAll(newAdjacents)
        }

        basinSize
    }.sortedDescending()

    val partTwo = basins[0] * basins[1] * basins[2]
    return partOne to partTwo
}
