package com.grappenmaker.aoc2021

import kotlin.math.floor

private val toIndices = { pair: Pair<Int, Int>, x: Int, y: Int, width: Int, height: Int ->
    val newX = x + pair.first
    val newY = y + pair.second
    val index = asIndex(newX, newY, width)

    if (newX !in 0 until width || newY !in 0 until height) null else index
}

fun asIndex(x: Int, y: Int, width: Int) = x + y * width
fun asXY(idx: Int, width: Int) = idx % width to floor(idx.toDouble() / width.toDouble()).toInt()

fun getAdjacents(x: Int, y: Int, width: Int, height: Int) =
    arrayOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1).mapNotNull { toIndices(it, x, y, width, height) }

fun getAdjacentsDiagonal(x: Int, y: Int, width: Int, height: Int) =
    getAdjacents(x, y, width, height) + arrayOf(-1 to -1, 1 to 1, -1 to 1, 1 to -1).mapNotNull { toIndices(it, x, y, width, height) }