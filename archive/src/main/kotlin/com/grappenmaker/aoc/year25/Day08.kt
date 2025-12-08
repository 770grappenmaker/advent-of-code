package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(8, 2025) {
    val pts = inputLines.map { l ->
        val (x, y, z) = l.split(",").map(String::toInt)
        Point3D(x, y, z)
    }

    infix fun Point3D.euclideanDistanceTo(other: Point3D) =
        (x - other.x).toLong().let { it * it } +
                (y - other.y).toLong().let { it * it } +
                (z - other.z).toLong().let { it * it }

    val conn = hashMapOf<Point3D, MutableSet<Point3D>>()
    val sorted = pts.permPairsExclusiveSeq()
        .filter { (a, b) -> b !in conn.getOrDefault(a, emptySet()) }
        .sortedBy { (a, b) -> a euclideanDistanceTo b }

    fun islands(early: Boolean = false): List<Int>? {
        val todo = pts.toMutableSet()
        val islands = mutableListOf<Int>()

        while (todo.isNotEmpty()) {
            val curr = todo.iterator().next()
            todo -= curr

            val island = hashSetOf(curr)
            val queue = ArrayDeque<Point3D>()
            queue += curr

            while (queue.isNotEmpty()) {
                val cc = queue.removeFirst()
                val nextt = conn.getOrDefault(cc, emptySet()) intersect todo
                todo -= nextt
                island += nextt
                queue.addAll(nextt)
            }

            if (early) return islands.takeIf { todo.isEmpty() }
            islands += island.size
        }

        return islands
    }

    fun isDone() = islands(early = true) != null

    var done = 0
    for ((a, b) in sorted) {
        if (b in conn.getOrDefault(a, emptySet())) continue
        if (++done == 1000) partOne = islands()!!.sorted().takeLast(3).product()

        conn.getOrPut(a) { hashSetOf() } += b
        conn.getOrPut(b) { hashSetOf() } += a

        if (isDone()) {
            partTwo = a.x * b.x
            break
        }
    }
}