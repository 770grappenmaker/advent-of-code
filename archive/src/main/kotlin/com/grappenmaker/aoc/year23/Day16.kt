package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day16() = puzzle(day = 16) {
    val g = inputLines.asCharGrid()
    val sd = mapOf(
        '-' to listOf(LEFT, RIGHT),
        '|' to listOf(UP, DOWN),
    )

    val md = mapOf(
        '/' to mapOf(
            UP to RIGHT,
            LEFT to DOWN,
            RIGHT to UP,
            DOWN to LEFT,
        ),
        '\\' to mapOf(
            UP to LEFT,
            LEFT to UP,
            RIGHT to DOWN,
            DOWN to RIGHT,
        )
    )

    data class State(val pos: Point = Point(0, 0), val dir: Direction = RIGHT)
    fun State.move(d: Direction = dir) = State(pos + d, d)

    fun solve(start: State): Int {
        val queue = queueOf(start)
        val seen = hashSetOf<Point>()
        val seenStates = hashSetOf<State>()

        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()
            if (p.pos !in g) continue

            seen += p.pos
            if (!seenStates.add(p)) continue

            queue += when (val t = g[p.pos]) {
                '.' -> listOf(p.move())
                in md.keys -> listOf(p.move(md.getValue(g[p.pos]).getValue(p.dir)))
                in sd.keys -> {
                    val ds = sd.getValue(t)
                    if (p.dir in ds) listOf(p.move()) else ds.map { p.move(it) }
                }

                else -> error("Impossible")
            }
        }

        return seen.size
    }

    partOne = solve(State()).s()
    partTwo = g.edges.flatMapTo(hashSetOf()) { it.allPoints() }.maxOf { p ->
        buildList {
            if (p.x == 0) add(RIGHT)
            if (p.y == 0) add(DOWN)
            if (p.x == g.maxX) add(LEFT)
            if (p.y == g.maxY) add(UP)
        }.maxOf { solve(State(p, it)) }
    }.s()
}