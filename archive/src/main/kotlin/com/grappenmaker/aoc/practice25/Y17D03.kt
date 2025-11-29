package com.grappenmaker.aoc.practice25

import com.grappenmaker.aoc.*

fun PuzzleSet.day03() = puzzle(day = 3) {
    val inp = input.ints().single()

    val g = InfiniteGrid<Int>()
    var pos = Point(0, 0)
    g[pos] = 1

    var incr = 1
    var left = incr
    var loops = 0
    var dir = Direction.RIGHT
    var curr = 0

    while (g[pos] < inp) {
        pos += dir
        curr++

        g[pos] = with (g) {
            pos.allAdjacent().sumOf { getOrNull(it) ?: 0 }
        }

        if (--left == 0) {
            loops = (loops + 1) % 2
            if (loops == 0) incr++
            dir = dir.next(-1)
            left = incr
        }
    }

    partTwo = g[pos]

    pos += dir * left
    curr += left
    loops = (loops + 1) % 2
    if (loops == 0) incr++
    dir = dir.next(-1)

    while (curr < inp) {
        val actual = minOf(inp - curr, incr)
        curr += actual
        pos += dir * actual

        loops = (loops + 1) % 2
        if (loops == 0) incr++
        dir = dir.next(-1)
    }

    partOne = pos.manhattanDistance - 1
}