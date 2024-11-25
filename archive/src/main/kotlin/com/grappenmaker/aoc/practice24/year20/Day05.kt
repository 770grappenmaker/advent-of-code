@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year20

import com.grappenmaker.aoc.*
import java.util.PriorityQueue
import kotlin.math.*

fun PuzzleSet.day05() = puzzle(day = 5) {
    var p1 = -1
    val ids = PriorityQueue<Int>()

    for (l in inputLines) {
        fun String.bs(hi: Int, f: Char): Int {
            var lor = 0
            var hir = hi

            for (c in this) {
                val m = (lor + hir) / 2
                if (c == f) hir = m - 1
                else lor = m + 1
            }

            return lor
        }

        val id = l.dropLast(3).bs(127, 'F') * 8 + l.takeLast(3).bs(7, 'L')
        p1 = maxOf(p1, id)
        ids += id
    }

    partOne = p1
    partTwo = generateSequence { ids.remove() }.windowed(2).first { (a, b) -> b - a != 1 }.first() + 1
}