@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    val w = 101
    val h = 103
    data class Rob(val pos: Point, val vel: Point)
    fun Rob.next() = copy(pos = Point((pos.x + vel.x).mod(w), (pos.y + vel.y).mod(h)))

    val initial = inputLines.map { l ->
        val (a, b, c, d) = l.ints()
        Rob(Point(a, b), Point(c, d))
    }

    fun seq() = generateSequence(initial) { it.map(Rob::next) }

    val state = seq().nth(100)
    var a = 0
    var b = 0
    var c = 0
    var d = 0
    for ((pos) in state) {
        if (pos.x == w / 2 || pos.y == h / 2) continue

        val left = pos.x < w / 2
        val top = pos.y < h / 2
        if (left && top) a++
        if (!left && !top) b++
        if (left && !top) c++
        if (!left && top) d++
    }

    partOne = a * b * c * d
    partTwo = seq().indexOfFirst { p -> p.allDistinctBy { it.pos } }
}