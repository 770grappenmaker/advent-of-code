@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day17() = puzzle(day = 17) {
    val defaultA = inputLines.first().substringAfterLast(' ').toLong()
    val program = inputLines.last().substringAfterLast(' ').split(',').map { it.toInt() }

    fun run(startA: Long = defaultA, expected: List<Int>? = null): List<Int> {
        var a = startA
        var b = 0L
        var c = 0L
        var ip = 0
        val outs = mutableListOf<Int>()

        while (ip in program.indices) {
            val curr = program[ip]
            val op = program[ip + 1]

            val literal = op.toLong()
            val combo = when(op) {
                4 -> a
                5 -> b
                6 -> c
                else -> op
            }.toLong()

            when(curr) {
                0 -> a /= (1 shl combo.toInt())
                1 -> b = b xor literal
                2 -> b = combo % 8
                3 -> if (a != 0L) {
                    ip = literal.toInt()
                    continue
                }

                4 -> b = b xor c
                5 -> {
                    outs += (combo % 8).toInt()
                    if (expected != null && expected[outs.lastIndex] != outs.last()) return outs
                }

                6 -> b = a / (1 shl combo.toInt())
                7 -> c = a / (1 shl combo.toInt())
            }

            ip += 2
        }

        return outs
    }

    // TODO: revisit this code in order to make the "detection" work properly
//    var toTry = listOf(0L)
//    for (i in ints.indices) {
//        val shift = i * 3
//        val nexts = mutableListOf<Long>()
//        outer@ for (num in toTry) {
//            for (j in 0L..7L) {
//                val next = num or (j shl shift)
//                val nums = compute(mutableListOf(next, 0, 0))
//                if (i < nums.size && nums[i] == ints[i]) {
//                    nexts += next
//                }
//            }
//        }
//
//        toTry = nexts
//    }

    partOne = run().joinToString(",")

    var cur = 1L shl 46
    var period = 1L
    var last = -1L
    var iter = 0L

    while (true) {
        val c = run(cur, program)
        if (c == program) {
            partTwo = cur
            break
        }

        if (c.size >= 6 && c.take(6) == program.take(6)) {
            if (last >= 0L) period *= (iter - last)
            last = iter
        }

        cur += period
        iter++
    }
}