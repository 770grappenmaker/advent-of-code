package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.PuzzleSet
import java.util.*

fun PuzzleSet.day15() = puzzle(day = 15) {
    // Get input
    val width = inputLines.first().length
    val height = inputLines.size
    val graph = inputLines.flatMap { line -> line.map { it.code - 48 } }

    // Part one and two
    partOne = solve(graph, width, height).s()
    partTwo = solve(graph, width, height, 5).s()
}

fun solve(graph: List<Int>, width: Int, height: Int, blocks: Int = 1): Int {
    val actualWidth = width * blocks
    val actualHeight = height * blocks

    val destination = Point(actualWidth - 1, actualHeight - 1)
    val queue = PriorityQueue<SearchNode> { (_, aRisk), (_, bRisk) -> aRisk - bRisk }
        .also { it.add(Point(0, 0) to 0) }
    val visited = mutableSetOf<Point>() // Dijkstra algorithm

    val getValue = { (x, y): Point ->
        val blockX = x / width
        val blockY = y / height
        val originalRisk = graph[asIndex(Point(x % width, y % height), width)]
        val newRisk = (originalRisk + blockX + blockY)
        newRisk.takeIf { it < 10 } ?: (newRisk - 9)
    }

    while (queue.isNotEmpty()) {
        val (point, risk) = queue.remove()
        if (point == destination) return risk

        if (point !in visited) {
            visited.add(point)
            getAdjacentsStraight(point, actualWidth, actualHeight)
                .map { asPoint(it, actualWidth) }
                .forEach { queue.offer(it to risk + getValue(it)) }
        }
    }

    error("Impossible? Never reached destination. Weird.")
}

typealias SearchNode = Pair<Point, Int>