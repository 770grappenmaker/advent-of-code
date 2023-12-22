package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day22() = puzzle(day = 22) {
    val bricks = inputLines.map { l ->
        l.split("~").map { p ->
            val (a, b, c) = p.split(",").map(String::toInt)
            Point3D(a, b, c)
        }.asPair()
    }

    val bp = bricks.map { (a, b) ->
        val ps = hashSetOf<Point3D>()

        for (x in a.x..b.x) {
            for (y in a.y..b.y) {
                for (z in a.z..b.z) {
                    ps += Point3D(x, y, z)
                }
            }
        }

        ps
    }

    fun List<Set<Point3D>>.step(): Pair<List<Set<Point3D>>, Int> {
        val new = mutableListOf<Set<Point3D>>()
        val fallen = hashSetOf<Point3D>()
        var a = 0

        for (b in this.sortedBy { b -> b.minOf { it.z } }) {
            var cb = b
            while (true) {
                val down = cb.mapToSet { p -> p.mapZ { it - 1 } }
                if (down.any { it in fallen || it.z <= 0 }) {
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
    val t = s.map { b ->
        val n = s.minusElement(b)
        n.step().second
    }

    partOne = t.countContains(0).s()
    partTwo = t.sum().s()
}