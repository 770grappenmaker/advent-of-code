package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day23() = puzzle(day = 23) {
    val g = inputLines.asCharGrid()
    val s = Point(1, 0)
    val e = Point(g.width - 2, g.height - 1)

    val ps = (g.points.filter { p ->
        g[p] != '#' && entries.filter { p + it in g && g[p + it] != '#' }.size > 2
    } + s + e).toSet()

    data class Entry(val to: Point, val d: Int)
    fun Point.next(partTwo: Boolean) = with(g) {
        if (partTwo) adjacentSides()
        else when (g[this@next]) {
            '^' -> listOf(this@next + UP)
            'v' -> listOf(this@next + DOWN)
            '<' -> listOf(this@next + LEFT)
            '>' -> listOf(this@next + RIGHT)
            else -> adjacentSides()
        }
    }.filter { g[it] != '#' }

    fun simplify(partTwo: Boolean): Map<Point, List<Entry>> {
        val gr = hashMapOf<Point, List<Entry>>()

        ps.forEach { p ->
            gr[p] = buildList {
                val queue = queueOf(p to 0)
                val seen = hashSetOf(p)

                queue.drain { (c, d) ->
                    if (c != p && c in ps) {
                        add(Entry(c, d))
                        return@drain
                    }

                    queue += c.next(partTwo).filter { seen.add(it) }.map { it to d + 1 }
                }
            }
        }

        return gr
    }

    fun solve(gr: Map<Point, List<Entry>>, seen: HashSet<Point> = hashSetOf(), curr: Point = s, dist: Int = 0): Int {
        if (curr == e) return dist
        if (curr in seen) return -1

        seen += curr
        return (gr.getValue(curr).maxOfOrNull { (to, d) -> solve(gr, seen, to, dist + d) } ?: -1).also { seen -= curr }
    }

    partOne = solve(simplify(false)).s()
    partTwo = solve(simplify(true)).s()
}