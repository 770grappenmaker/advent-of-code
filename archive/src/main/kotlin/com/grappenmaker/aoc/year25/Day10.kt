package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.simplePuzzle
import com.microsoft.z3.Expr
import com.microsoft.z3.IntSort
import com.sschr15.z3kt.*

fun main() = simplePuzzle(10, 2025) {
    partOne = inputLines.sumOf { l ->
        val parts = l.split(" ")
        val goal = parts.first().drop(1).dropLast(1).map { it == '#' }
        val buttons = parts.drop(1).dropLast(1).map { it.drop(1).dropLast(1).split(",").map(String::toInt) }

        val queue = ArrayDeque<Pair<List<Boolean>, Int>>()
        queue += goal.map { false } to 0
        val seen = hashSetOf<List<Boolean>>()

        while (queue.isNotEmpty()) {
            val (state, presses) = queue.removeLast()
            if (state == goal) return@sumOf presses

            for (b in buttons) {
                val res = state.toMutableList()
                for (i in b) res[i] = !res[i]
                if (seen.add(res)) queue.addFirst(res to (presses + 1))
            }
        }

        error("No possibility found")
    }

    z3 {
        partTwo = inputLines.sumOf { l ->
            val parts = l.split(" ")
            val buttons = parts.drop(1).dropLast(1).map { it.drop(1).dropLast(1).split(",").map(String::toInt).toSet() }
            val jolts = parts.last().drop(1).dropLast(1).split(",").map(String::toInt)
            val buttonVars = buttons.indices.map { mkIntConst("b$it") }

            mkOptimize().run {
                buttonVars.forEach { Add(it gte 0) }
                for (i in jolts.indices) Add(buttons.indices.asSequence().filter { i in buttons[it] }
                    .fold(mkInt(0)) { acc: Expr<IntSort>, curr -> mkAdd(acc, buttonVars[curr]) } eq jolts[i])

                val sum = buttonVars.fold(mkInt(0)) { acc: Expr<IntSort>, curr -> mkAdd(acc, curr) }
                MkMinimize(sum)
                Check()
                model.eval(sum, true).toLong()
            }
        }
    }
}