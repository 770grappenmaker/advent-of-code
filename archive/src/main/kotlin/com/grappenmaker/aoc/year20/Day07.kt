package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(7) {
    val reqs = mutableMapOf<String, List<Pair<Int, String>>>()

    for (l in inputLines) {
        val p = l.split(" contain ")
        val t = p[0].substringBeforeLast(' ')

        val ls = p[1].split(", ")
        reqs[t] = if (ls[0].startsWith("no")) emptyList() else ls.map {
            val s = it.split(" ")
            s[0].toInt() to s.drop(1).dropLast(1).joinToString(" ")
        }
    }

    var p1 = 0
    for (k in reqs.keys) {
        if (k == "shiny gold") continue

        val seen = hashSetOf(k)
        val queue = ArrayDeque<String>()
        queue += k

        while (queue.isNotEmpty()) {
            val curr = queue.removeLast()
            if (curr == "shiny gold") {
                p1++
                break
            }

            reqs[curr]?.forEach { (_, v) -> if (seen.add(v)) queue.addLast(v) }
        }
    }

    var p2 = 0
    val queue = ArrayDeque<String>()
    queue += "shiny gold"

    while (queue.isNotEmpty()) {
        val curr = queue.removeLast()
        reqs[curr]?.forEach { (k, v) ->
            p2 += k
            repeat(k) { queue.addLast(v) }
        }
    }

    partOne = p1
    partTwo = p2
}