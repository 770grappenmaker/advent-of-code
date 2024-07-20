package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.absoluteValue

@PuzzleEntry
fun PuzzleSet.day10() = puzzle(day = 10) {
    val curr = inputLines.map {
        val (x, y, vx, vy) = it.splitInts()
        Point(x, y) to Point(vx, vy)
    }

    fun Point.product() = x.toLong() * y.toLong()
    fun List<Pair<Point, Point>>.map() = map { (a) -> a }
    fun List<Point>.area() = (maxBound() - minBound()).product().absoluteValue

    val (result, idx) =
        generateSequence(curr to 0) { (state, idx) -> state.map { (point, vel) -> point + vel to vel } to idx + 1 }
            .zipWithNext().first { (from, to) -> from.first.map().area() < to.first.map().area() }.first

    partOne = "\n" + result.map().minimizeEmpty().asBooleanGrid().debug(on = "oo ", off = "   ")
    partTwo = idx.s()
}
