@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day12simple() = puzzle(day = 12) {
    val reg = MutableList(4) { 0 }
    var ptr = 0

    fun run() {
        while (ptr in inputLines.indices) {
            val l = inputLines[ptr]
            val w = l.split(" ")
            val insn = w[0]
            val args = w.drop(1)
            fun String.get() = toIntOrNull() ?: reg[single() - 'a']

            when (insn) {
                "cpy" -> reg[args[1].single() - 'a'] = args[0].get()
                "inc" -> reg[args[0].single() - 'a']++
                "dec" -> reg[args[0].single() - 'a']--
                "jnz" -> if (args[0].get() != 0) ptr += args[1].get() - 1
            }

            ptr++
        }
    }

    run()
    partOne = reg[0]

    ptr = 0
    reg.mapInPlace { 0 }
    reg[2] = 1
    run()
    partTwo = reg[0]
}