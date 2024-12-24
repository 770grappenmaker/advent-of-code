@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.absoluteValue

fun PuzzleSet.day24() = puzzle(day = 24) {
    val (fp, sp) = input.doubleLines()
    val regs: MutableMap<String, () -> Boolean> = hashMapOf()

    for (l in fp.lines()) {
        val (n, s) = l.split(": ")
        val b = s == "1"
        regs[n] = { b }
    }

    fun get(f: String) = regs.gv(f)().also { regs[f] = { it } }

    for (l in sp.lines()) {
        val (left, right) = l.split(" -> ")
        val (lhs, op, rhs) = left.split(" ")

        regs[right] = {
            val l = get(lhs)
            val r = get(rhs)
            when (op) {
                "AND" -> l && r
                "OR" -> l || r
                "XOR" -> l xor r
                else -> error("")
            }
        }
    }

    partOne = regs.keys.filter { it.startsWith('z') }.sorted()
        .foldIndexed(0L) { idx, acc, curr -> acc or ((if (get(curr)) 1L else 0L) shl idx) }
}