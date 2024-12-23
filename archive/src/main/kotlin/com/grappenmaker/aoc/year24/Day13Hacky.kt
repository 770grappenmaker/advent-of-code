@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.microsoft.z3.Model
import com.microsoft.z3.Optimize
import com.microsoft.z3.Status
import com.sschr15.z3kt.Z3Context
import com.sschr15.z3kt.int
import com.sschr15.z3kt.z3

fun PuzzleSet.day13() = puzzle(day = 13) {
    var p1 = 0L
    var p2 = 0L

    z3 {
        for (ls in input.doubleLines()) {
            val (a, b, p) = ls.lines().map { it.ints() }.map { (a, b) -> Point(a, b) }
            val i by int
            val j by int

            fun solve(px: Long, py: Long): Long {
                val model = optimize {
                    Add(a.x * i + b.x * j eq px)
                    Add(a.y * i + b.y * j eq py)
                    MkMinimize(3 * i + j)
                }

                if (model == null) return 0
                return model.evaluate(3 * i + j, true).toLong()
            }

            p1 += solve(p.x.toLong(), p.y.toLong())
            p2 += solve(p.x + 10000000000000, p.y + 10000000000000)
        }
    }

    partOne = p1
    partTwo = p2
}

inline fun Z3Context.optimize(block: Optimize.() -> Unit): Model? {
    val solver = mkOptimize()
    solver.block()
    return solver.Check().let {
        when (it) {
            Status.SATISFIABLE -> solver.model
            Status.UNSATISFIABLE -> null
            Status.UNKNOWN -> error("Solver failed to determine satisfiability")
            null -> error("Solver returned null status on satisfiability check")
        }
    }
}