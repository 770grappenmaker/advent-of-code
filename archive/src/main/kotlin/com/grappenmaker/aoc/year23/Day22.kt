package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day22() = puzzle(day = 22) {
    val bp = inputLines.map { l ->
        val (a, b) = l.split("~").map { p ->
            val (a, b, c) = p.split(",").map(String::toInt)
            Point3D(a, b, c)
        }

        (a..b).points
    }.sortedBy { b -> b.minOf { it.z } }

    fun List<List<Point3D>>.step(): Pair<List<List<Point3D>>, Int> {
        val new = mutableListOf<List<Point3D>>()
        val fallen = hashSetOf<Point3D>()
        var a = 0

        for (b in this) {
            var cb = b
            while (true) {
                val down = cb.map { p -> p.mapZ { it - 1 } }
                if (down.any { it.z <= 0 || it in fallen }) {
                    new += cb
                    fallen += cb
                    if (cb != b) a++
                    break
                }

                cb = down
            }
        }

        return new to a
    }

    val (s) = bp.step()
    val t = s.map { b -> s.minusElement(b).step().second }
    partOne = t.countContains(0).s()
    partTwo = t.sum().s()
}