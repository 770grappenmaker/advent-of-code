package com.grappenmaker.aoc2021

import kotlin.math.floor

private val toIndices = { pair: Pair<Int, Int>, (x, y): Point, width: Int, height: Int ->
    val newX = x + pair.first
    val newY = y + pair.second
    val index = asIndex(newX, newY, width)

    if (newX in 0 until width && newY in 0 until height) index else null
}

fun asIndex(point: Point, width: Int) = asIndex(point.x, point.y, width)
fun asIndex(x: Int, y: Int, width: Int) = x + y * width
fun asPoint(idx: Int, width: Int) = Point(idx % width, floor(idx.toDouble() / width.toDouble()).toInt())

fun getAdjacentsStraight(point: Point, width: Int, height: Int) =
    arrayOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1).mapNotNull { toIndices(it, point, width, height) }

fun getAdjacentsDiagonal(point: Point, width: Int, height: Int) =
    arrayOf(-1 to -1, 1 to 1, -1 to 1, 1 to -1).mapNotNull { toIndices(it, point, width, height) }

fun getAllAdjacents(point: Point, width: Int, height: Int) =
    getAdjacentsStraight(point, width, height) + getAdjacentsDiagonal(point, width, height)