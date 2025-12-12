package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day12() = puzzle(day = 12) {
    val parts = rawInput.split("\n\n")
    val gs = parts.dropLast(1)
    val ggs = gs.map { l -> l.lines().drop(1).asGrid { it == '#' } }

    var ans = 0
    val memo = hashMapOf<Pair<BooleanGrid, List<Int>>, Boolean>()

    for (p in parts.last().lines()) {
        val (s, ns) = p.split(": ")
        val (w, h) = s.ints()
        val empty = booleanGrid(w, h)

        val area = w * h
        if (area < ns.ints().sumOfIndexed { idx, v -> v * ggs[idx].findPointsValued(true).size }) continue

        fun recur(big: BooleanGrid, left: List<Int>): Boolean = memo.getOrPut(big to left) {
            val firstToFix = left.indices.maxBy { left[it] }
            if (left[firstToFix] <= 0) return@getOrPut true

            val toFix = ggs[firstToFix]
            for (poss in toFix.orientations()) for (x in 0..big.width - toFix.width) for (y in 0..big.height - toFix.height) {
                if (poss.findPointsValued(true).any { p -> big[p + Point(x, y)] }) continue

                val next = big.asMutableGrid()
                for (p in toFix.pointsSequence.filter { toFix[it] }) next[p + Point(x, y)] = true

                val left = left.toMutableList()
                left[firstToFix]--

                if (recur(next, left)) return@getOrPut true
            }

            return@getOrPut false
        }

        if (recur(empty, ns.ints())) ans++
    }

    partOne = ans
    partTwo = "This problem is weird and stupid... anyway Merry (early) Christmas!"
}