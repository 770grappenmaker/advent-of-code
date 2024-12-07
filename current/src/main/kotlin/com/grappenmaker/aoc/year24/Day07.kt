@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

// remember that input is trimmed
fun PuzzleSet.day07() = puzzle(day = 7) {
    data class Entry(val sum: Long, val operands: List<Long>)

    val entries = inputLines.mapTo(mutableListOf()) { l ->
        val parts = l.split(" ")
        val sum = parts.first().dropLast(1).toLong()
        val operands = parts.drop(1).map { it.toLong() }
        Entry(sum, operands)
    }

    fun solve(p2: Boolean): Long {
        var ans = 0L

        entries.removeIf { (sum, operands) ->
            val work = ArrayDeque<List<Int>>()
            work.addLast(emptyList())

            inner@ while (work.isNotEmpty()) {
                val ops = work.removeLast()

                if (ops.size != operands.size - 1) {
                    work.addLast(ops + 0)
                    work.addLast(ops + 1)
                    if (p2) work.addLast(ops + 2)
                    continue
                }

                val todo = operands.asReversed().toQueue()
                for (op in ops) {
                    val lhs = todo.removeLast()
                    val rhs = todo.removeLast()

                    if (lhs > sum) continue@inner
                    if (rhs > sum) continue@inner

                    todo.addLast(
                        when (op) {
                            1 -> lhs + rhs
                            2 -> "$lhs$rhs".toLong()
                            else -> lhs * rhs
                        }
                    )
                }

                if (todo.removeLast() == sum) {
                    ans += sum
                    return@removeIf true
                }
            }

            false
        }

        return ans
    }

    val p1ans = solve(false)
    partOne = p1ans
    partTwo = solve(true) + p1ans
}