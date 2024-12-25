@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.absoluteValue

@PuzzleEntry
fun PuzzleSet.day01() = puzzle(day = 1) {
    val al = mutableListOf<Int>()
    val bl = mutableListOf<Int>()
    val freq = IntArray(100000)

    for (l in inputLines) {
        val (a, b) = l.split("""   """).map { it.toInt() }
        al += a
        bl += b
        freq[b]++
    }

    al.sort()
    bl.sort()

    partOne = al.sumOfIndexed { idx, n -> (n - bl[idx]).absoluteValue }
    partTwo = al.sumOf { it * freq[it] }
}