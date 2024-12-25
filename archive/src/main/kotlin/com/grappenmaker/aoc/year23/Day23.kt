package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day23() = puzzle(day = 23) {
    val g = inputLines.asCharGrid()
    val s = Point(1, 0)
    val e = Point(g.width - 2, g.height - 1)

    val ps = (g.points.filter { p ->
        g[p] != '#' && entries.filter { p + it in g && g[p + it] != '#' }.size > 2
    } + s + e).toSet()

    fun find(
        gr: Map<Point, List<Edge<Point>>>,
        seen: HashSet<Point> = hashSetOf(),
        curr: Point = s,
        dist: Int = 0
    ): Int {
        if (curr == e) return dist
        if (curr in seen) return -1

        seen += curr
        return (gr.getValue(curr).maxOfOrNull { (to, d) -> find(gr, seen, to, dist + d) } ?: -1).also { seen -= curr }
    }

    fun solve(partTwo: Boolean) = find(ps.asWeightedGraph { p ->
        (if (partTwo) with(g) { p.adjacentSides() }
        else when (g[p]) {
            '^' -> listOf(p + UP)
            'v' -> listOf(p + DOWN)
            '<' -> listOf(p + LEFT)
            '>' -> listOf(p + RIGHT)
            else -> with(g) { p.adjacentSides() }
        }).filter { g[it] != '#' }
    })

    partOne = solve(false).toString()
    partTwo = solve(true).toString()
}