package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day10() {
    fun CharGrid.pts() = (2..<height - 2).flatMap { y -> (2..<width - 2).map { x -> Point(x, y) } }

    fun MutableCharGrid.solve(difficult: Boolean): String {
        val allPts = pts()
        val pts = allPts.filterTo(hashSetOf()) { this[it] == '.' }
        val iter = pts.iterator()

        while (iter.hasNext()) {
            val p = iter.next()
            val r = rowValues(p.y).toSet() - '.'
            val c = columnValues(p.x).toSet() - '.'
            val v = r.intersect(c).singleOrNull() ?: continue
            this[p] = v

            iter.remove()
        }

        var last = pts.size
        while (difficult && pts.isNotEmpty()) {
            val iter = pts.iterator()

            while (iter.hasNext()) {
                val p = iter.next()

                val edges = listOf(
                    Point(0, p.y),
                    Point(1, p.y),
                    Point(p.x, 0),
                    Point(p.x, 1),
                    Point(maxX, p.y),
                    Point(maxX - 1, p.y),
                    Point(p.x, maxY),
                    Point(p.x, maxY - 1)
                )

                val toSolve = edges.singleOrNull { this[it] == '?' } ?: continue

                val r = rowValues(p.y)
                val c = columnValues(p.x)
                val poss =
                    (r.take(2).toSet() + c.take(2).toSet() +
                            r.takeLast(2).toSet() + c.takeLast(2).toSet() - '?').toMutableSet()

                poss -= r.drop(2).dropLast(2).toSet()
                poss -= c.drop(2).dropLast(2).toSet()

                this[p] = poss.singleOrNull() ?: continue
                this[toSolve] = this[p]

                iter.remove()
            }

            if (pts.size == last) return ""
            last = pts.size
        }

        return allPts.joinToString("") { this[it].toString() }
    }

    fun ECInput.solve(step: Int, difficult: Boolean = false): List<MutableSubGrid<Char>> {
        val g = inputLines.padSameLength().asCharGrid().asMutableGrid()
        val corners = (0..g.width - 8 step step).flatMap { x ->
            (0..g.height - 8 step step).map { Point(x, it) }
        }

        val gs = corners.map { g.mutableSubGrid(it, 8, 8) }
        var unsolved = g.countContains('.')

        do {
            for (gg in gs) gg.solve(difficult)

            val newUnsolved = g.countContains('.')
            if (newUnsolved == unsolved) break

            unsolved = newUnsolved
        } while (difficult)

        return gs
    }

    fun ECInput.solveLater(step: Int, difficult: Boolean = false) = solve(step, difficult).sumOf { g ->
        val w = g.solve(difficult)
        var sum = 0L

        for ((idx, c) in w.withIndex()) {
            sum += (idx + 1) * (c - 'A' + 1)
        }

        sum
    }

    val p1g = partOneInput.solve(8).single()
    partOne = p1g.pts().joinToString("") { p1g[it].toString() }
    partTwo = partTwoInput.solveLater(9)
    partThree = partThreeInput.solveLater(6, difficult = true)
}
