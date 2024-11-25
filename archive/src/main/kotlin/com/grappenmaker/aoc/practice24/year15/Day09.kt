@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year15

import com.grappenmaker.aoc.*
import java.util.PriorityQueue

fun PuzzleSet.day09() = puzzle(day = 9) {
    val g = hashMapOf<String, MutableMap<String, Int>>()

    for (l in inputLines) {
        val (f, _, t, _, d) = l.split(" ")
        val dd = d.toInt()

        g.getOrPut(f) { hashMapOf() }[t] = dd
        g.getOrPut(t) { hashMapOf() }[f] = dd
    }

    data class Node(val d: Int, val s: Set<String>, val l: String)
    val poss = g.keys

    fun eval(start: String, reverse: Boolean): Int {
        val queue = PriorityQueue(compareBy<Node> { it.d }.let { if (reverse) it.reversed() else it })
        queue += Node(0, setOf(start), start)

        while (queue.isNotEmpty()) {
            val (d, seen, last) = queue.remove()
            if (seen == poss) return d

            for ((t, dd) in g.getValue(last)) {
                if (t !in seen) queue.add(Node(d + dd, seen + t, t))
            }
        }

        return 9999
    }

    partOne = poss.minOf { eval(it, false) }
    partTwo = poss.maxOf { eval(it, true) }
}