@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.util.*

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    val (fp, sp) = input.doubleLines()

    fun solve(builder: (String) -> String = { it }): Long {
        val g = fp.lines().map(builder).asCharGrid().asMutableGrid()
        var r = g.findPointsValued('@').single()
        g[r] = '.'

        for (c in sp) {
            val dir = when (c) {
                '^' -> UP
                'v' -> DOWN
                '>' -> RIGHT
                '<' -> LEFT
                else -> continue
            }

            val ptsToMove = hashSetOf<Point>()
            val todo = ArrayDeque<Point>()
            todo += r

            while (todo.isNotEmpty()) {
                val p = todo.removeFirst()
                if (!ptsToMove.add(p)) continue

                val n = p + dir
                if (n in g && (g[n] != '.' && g[n] != '#')) todo += n

                if (g[p] == '[') todo += (p + RIGHT)
                if (g[p] == ']') todo += (p + LEFT)
            }

            val allSafe = ptsToMove.all {
                val pd = it + dir
                pd in ptsToMove || g[pd] == '.'
            }

            if (!allSafe) continue

            for (p in ptsToMove.sortedBy {
                when (dir) {
                    LEFT -> it.x
                    RIGHT -> -it.x
                    UP -> it.y
                    DOWN -> -it.y
                }
            }) {
                if (p == r) continue
                with(g) { Collections.swap(g, p.toIndex(), (p + dir).toIndex()) }
            }

            r += dir
        }

        return g.points.sumOf { p -> if (g[p] == '[' || g[p] == 'O') 100L * p.y + p.x else 0L }
    }

    partOne = solve()
    partTwo = solve { l ->
        buildString {
            for (c in l) when (c) {
                '#' -> append("##")
                'O' -> append("[]")
                '.' -> append("..")
                '@' -> append("@.")
                else -> continue
            }
        }
    }
}