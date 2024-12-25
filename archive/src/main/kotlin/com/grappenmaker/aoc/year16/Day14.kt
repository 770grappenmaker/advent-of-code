package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.allIdentical
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    fun solve(iter: Int = 1): String {
        fun hash(idx: Int): String {
            var curr = "$input$idx"
            repeat(iter) { curr = curr.digest() }
            return curr
        }

        val roll = MutableList(1000) { hash(it) }
        var hashes = 0
        var idx = 0

        while (hashes < 64) {
            try {
                val curr = roll[idx % 1000]
                roll[idx % 1000] = hash(idx + 1000)

                val c = (curr.windowedSequence(3).firstOrNull { it.deepen().allIdentical() } ?: continue).first()
                val target = c.toString().repeat(5)
                if (roll.any { target in it }) hashes++
            } finally {
                idx++
            }
        }

        return (idx - 1).toString()
    }

    partOne = solve()
    partTwo = solve(2016 + 1)
}

fun String.digest() =
    md5.digest(encodeToByteArray()).joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }