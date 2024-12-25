@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.util.*

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(day = 21) {
    data class MemoKey(val left: Int, val p: Point, val t: Char, val ref: NonDataBox<Map<Char, Point>>)

    val solver = object {
        private fun String.parseGrid(): NonDataBox<Map<Char, Point>> {
            val g = trimIndent().lines().asCharGrid()
            return NonDataBox(g.pointsSequence.associateBy { g[it] })
        }

        private val numpad = """
        789
        456
        123
         0A
        """.parseGrid()

        private val dirpad = """
         ^A
        <v>
        """.parseGrid()

        private val memo = hashMapOf<MemoKey, Long>()

        private fun pressTo(left: Int, p: Point, t: Char, g: NonDataBox<Map<Char, Point>>): Long =
            memo.getOrPut(MemoKey(left, p, t, g)) {
                val death = g.value.gv(' ')
                val end = g.value.gv(t)

                val queue = ArrayDeque<Pair<List<Char>, Point>>()
                queue += emptyList<Char>() to p

                var ans = Long.MAX_VALUE

                while (queue.isNotEmpty()) {
                    val (dirs, curr) = queue.removeLast()
                    if (curr == end) {
                        ans = minOf(ans, makePath(dirs + 'A', left - 1, dirpad))
                        continue
                    }

                    val diff = end - curr

                    for (d in Direction.entries) {
                        if (d.dx * diff.x <= 0 && d.dy * diff.y <= 0) continue

                        val c = when (d) {
                            UP -> '^'
                            RIGHT -> '>'
                            DOWN -> 'v'
                            LEFT -> '<'
                        }

                        val np = curr + d
                        if (np == death) continue

                        val nd = dirs + c
                        queue.addFirst(nd to np)
                    }
                }

                ans
            }

        fun makePath(look: List<Char>, left: Int, g: NonDataBox<Map<Char, Point>> = numpad): Long {
            if (left == 0) return look.size.toLong()

            var ans = 0L
            var pos = g.value.gv('A')

            for (dir in look) {
                ans += pressTo(left, pos, dir, g)
                pos = g.value.gv(dir)
            }

            return ans
        }
    }

    fun solve(n: Int) = inputLines.sumOf { solver.makePath(it.toList(), n + 1) * it.take(3).toLong() }

    partOne = solve(2)
    partTwo = solve(25)
}