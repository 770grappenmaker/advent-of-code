@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day23() = puzzle(day = 23) {
    val g = hashMapOf<String, MutableSet<String>>()

    for (l in inputLines) {
        val (f, t) = l.split('-')
        g.getOrPut(f) { hashSetOf() } += t
        g.getOrPut(t) { hashSetOf() } += f
    }

    val combis = hashSetOf<Set<String>>()

    for (p1 in g.keys) for (p2 in g.gv(p1)) for (p3 in g.gv(p2) intersect g.gv(p1)) {
        if (!p1.startsWith('t') && !p2.startsWith('t') && !p3.startsWith('t')) continue
        combis += setOf(p1, p2, p3)
    }

    partOne = combis.size
    partTwo = sequence {
        val todo = g.keys.toHashSet()

        while (todo.isNotEmpty()) {
            val curr = todo.first()
            todo -= curr
            val ans = hashSetOf(curr)

            for (n in g.gv(curr)) if (n !in ans && ans.all { n in g.gv(it) }) ans += n
            yield(ans)
        }
    }.maxBy { it.size }.sorted().joinToString(",")
}