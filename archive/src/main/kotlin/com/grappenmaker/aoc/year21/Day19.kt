package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(day = 19) {
    fun Point3D.permutations() = listOf(
        this,
        Point3D(x, -y, -z),
        Point3D(x, -z, y),
        Point3D(-y, -z, x),
        Point3D(y, -z, -x),
        Point3D(-x, -z, -y),
        //
        Point3D(-y, x, z),
        Point3D(y, x, -z),
        Point3D(z, x, y),
        Point3D(z, -y, x),
        Point3D(z, y, -x),
        Point3D(z, -x, -y),
        //
        Point3D(-x, -y, z),
        Point3D(-x, y, -z),
        Point3D(-x, z, y),
        Point3D(y, z, x),
        Point3D(-y, z, -x),
        Point3D(x, z, -y),
        //
        Point3D(y, -x, z),
        Point3D(-y, -x, -z),
        Point3D(-z, -x, y),
        Point3D(-z, y, x),
        Point3D(-z, -y, -x),
        Point3D(-z, x, -y),
    )

    val perms = input.split("\n\n").map { l ->
        l.lines().drop(1).map {
            val (x, y, z) = it.split(",").map(String::toInt)
            Point3D(x, y, z).permutations()
        }.swapOrder()
    }

    val corrected = perms.first().first().toMutableSet()
    val todo = queueOf(perms.drop(1))
    val found = mutableListOf(Point3D(0, 0, 0))

    label@ while (todo.isNotEmpty()) {
        val curr = todo.removeFirst()

        // when the first ridiculous algorithm you think of works
        for (poss in curr) {
            for (relCorrected in poss) {
                for (target in corrected) {
                    val delta = target - relCorrected
                    val mapped = poss.map { it + delta }

                    if (mapped.count { it in corrected } >= 12) {
                        found += delta
                        corrected += mapped
                        continue@label
                    }
                }
            }
        }

        todo.addLast(curr)
    }

    partOne = corrected.size.s()
    partTwo = found.maxOf { a -> (found - a).maxOf { b -> a manhattanDistanceTo b } }.s()
}