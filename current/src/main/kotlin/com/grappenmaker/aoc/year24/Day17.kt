@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue
import com.grappenmaker.aoc.Direction.*

fun PuzzleSet.day17() = puzzle(day = 17) {
    val defaultA = inputLines.first().substringAfterLast(' ').toLong()
    val program = inputLines.last().substringAfterLast(' ').split(',').map { it.toInt() }
    val compiled = program.parseInsns().createCompiled()

    partOne = compiled.run(defaultA, 0, 0)

    var toTry = setOf(0L)
    for (i in program.asReversed()) {
        val nexts = hashSetOf<Long>()

        for (num in toTry) for (j in 0L..7L) {
            val next = (num shl 3) or j
            val res = compiled.runSingle(next, 0, 0).single()
            if (res == i) nexts += next
        }

        toTry = nexts
    }

    partTwo = toTry.filter { compiled.run(it, 0, 0) == program }.min()
}