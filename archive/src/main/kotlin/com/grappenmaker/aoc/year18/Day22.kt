package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.math.BigInteger

// I don't like this code
private val mod = 20183.toBigInteger()
private val xTimes = 16807L.toBigInteger()
private val yTimes = 48271L.toBigInteger()
@PuzzleEntry

fun PuzzleSet.day22() = puzzle {
    val (depth, targetX, targetY) = rawInput.splitInts()
    val depthBig = depth.toBigInteger()
    val target = Point(targetX, targetY)

    val cache = mutableMapOf<Point, BigInteger>(
        Point(0, 0) to BigInteger.ZERO,
        target to BigInteger.ZERO,
    )

    val grid = grid(targetX + 1, targetY + 1) { it.caveType(cache, depthBig) }
    partOne = grid.elements.sum().toString()

    data class State(val equipped: Tool = Tool.TORCH, val pos: Point = Point(0, 0), val justEquipped: Boolean = false)

    partTwo = dijkstra(
        initial = State(),
        isEnd = { it.pos == target && it.equipped == Tool.TORCH },
        neighbors = { curr ->
            curr.pos.adjacentSidesInf()
                .filter { it.x >= 0 && it.y >= 0 && curr.equipped.isAllowed(it.caveType(cache, depthBig)) }
                .map { curr.copy(pos = it, justEquipped = false) } + curr.copy(
                equipped = curr.equipped.other(curr.pos.caveType(cache, depthBig)),
                justEquipped = true
            )
        },
        findCost = { if (it.justEquipped) 7 else 1 }
    )?.cost.toString()
}

private fun Point.geoIndex(cache: MutableMap<Point, BigInteger>, depth: BigInteger): BigInteger = cache.getOrPut(this) {
    when {
        y == 0 -> (x.toBigInteger() * xTimes) % mod
        x == 0 -> (y.toBigInteger() * yTimes) % mod
        else -> (Point(x - 1, y).erosion(cache, depth) * Point(x, y - 1).erosion(cache, depth)) % mod
    }
}

private fun Point.erosion(cache: MutableMap<Point, BigInteger>, depth: BigInteger) =
    (geoIndex(cache, depth) + depth) % mod

private fun Point.caveType(cache: MutableMap<Point, BigInteger>, depth: BigInteger) =
    erosion(cache, depth).intValueExact() % 3

enum class Tool {
    TORCH, CLIMB, NEITHER;

    fun other(type: Int) = when (type) {
        0 -> if (this == CLIMB) TORCH else CLIMB
        1 -> if (this == CLIMB) NEITHER else CLIMB
        2 -> if (this == TORCH) NEITHER else TORCH
        else -> error("Impossible")
    }

    fun isAllowed(type: Int) = when (type) {
        0 -> this == TORCH || this == CLIMB
        1 -> this == NEITHER || this == CLIMB
        2 -> this == TORCH || this == NEITHER
        else -> error("Impossible")
    }
}