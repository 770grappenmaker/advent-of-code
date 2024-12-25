@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

fun fastlog10(num: Long): Long {
    if (num < 1000000000L) {
        if (num < 10000L) {
            if (num < 10L) {
                if (num < 1L) return 1L
                return 10L
            }

            if (num < 1000L) {
                if (num < 100L) return 100L
                return 1000L
            }
            return 10000L
        }

        if (num < 100000L) return 100000L
        if (num < 1000000L) return 1000000L
        if (num < 10000000L) return 10000000L
        if (num < 100000000L) return 100000000L
        return 1000000000L
    }

    if (num < 100000000000000L) {
        if (num < 10000000000L) return 10000000000L
        if (num < 100000000000L) return 100000000000L
        if (num < 1000000000000L) return 1000000000000L
        if (num < 10000000000000L) return 10000000000000L
        return 100000000000000L
    }

    if (num < 10000000000000000L) {
        if (num < 1000000000000000L) return 1000000000000000L
        return 10000000000000000L
    }

    return if (num < 100000000000000000L) 100000000000000000L else 1000000000000000000L
}

data class Entry(val sum: Long, val operands: List<Long>)

class Worker(private val entries: List<Entry>, private val p2: Boolean) : Thread() {
    val failed = mutableListOf<Entry>()
    var ans = 0L
        private set

    private val work = IntArray(23)
    private var workPtr = 0

    @Suppress("NOTHING_TO_INLINE")
    private inline fun addWork(num: Int) {
        work[workPtr++] = num
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun takeWork() = work[--workPtr]

    override fun run() {
        outer@ for (e in entries) {
            val (sum, operands) = e
            addWork(0)

            val shift = (operands.size - 2) shl 1

            inner@ while (workPtr > 0) {
                val opsVal = takeWork()
                val ops = opsVal and 0x3fffffff
                val flag = opsVal and 0x40000000

                if (ops ushr shift == 0) {
                    val shifted = (ops shl 2) or flag
                    addWork(shifted or 1)
                    addWork(shifted or 2)
                    if (p2) addWork(shifted or 0x40000003)
                    continue
                }

                if (p2 && flag == 0) continue

                var curr = operands.first()
                var ptr = 1
                var opLocal = ops

                while (opLocal != 0) {
                    val lhs = curr
                    val rhs = operands[ptr++]

                    if (lhs > sum) continue@inner
                    if (rhs > sum) continue@inner

                    curr = when (opLocal and 3) {
                        1 -> lhs + rhs
                        3 -> fastlog10(rhs) * lhs + rhs
                        else -> lhs * rhs
                    }

                    opLocal = opLocal ushr 2
                }

                if (curr == sum) {
                    ans += sum
                    workPtr = 0
                    continue@outer
                }
            }

            if (!p2) failed += e
        }
    }
}

@PuzzleEntry
fun PuzzleSet.day07() = puzzle(day = 7) {
    val entries = inputLines.map { l ->
        val parts = l.split(' ')
        val sum = parts.first().dropLast(1).toLong()
        val operands = parts.drop(1).map { it.toLong() }
        Entry(sum, operands)
    }

    fun run(totalWork: List<Entry>, p2: Boolean) = totalWork.chunked(totalWork.size / 12)
        .map { work -> Worker(work, p2).also { it.start() } }.onEach { it.join() }

    val p1 = run(entries, false)
    val partial = p1.sumOf { it.ans }
    partOne = partial
    partTwo = run(p1.flatMap { it.failed }, true).sumOf { it.ans } + partial
}