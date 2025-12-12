package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day08() = puzzle(day = 8) {
    val pts = inputLines.map { l ->
        val (x, y, z) = l.split(",").map(String::toInt)
        Point3D(x, y, z)
    }

    infix fun Point3D.euclideanDistanceTo(other: Point3D) =
        (x - other.x).toLong().let { it * it } +
                (y - other.y).toLong().let { it * it } +
                (z - other.z).toLong().let { it * it }

    val sorted = pts.permPairsExclusiveSeq().sortedBy { (a, b) -> a euclideanDistanceTo b }
    val uf = UnionFind<Point3D>()
    val direct = hashSetOf<UnorderedPair<Point3D>>()

    var done = 0
    for ((a, b) in sorted) {
        if (!direct.add(UnorderedPair(a, b))) continue
        if (++done == 1000) partOne = uf.islands.values.map { it.size }.sortedDescending().take(3).product()

        uf.add(a, b)

        if (uf.roots.size == 1 && done > 1000) {
            partTwo = a.x * b.x
            break
        }
    }
}