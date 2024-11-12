@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day04() = puzzle(day = 4) {
    var p1 = 0
    var p2 = 0

    for (l in inputLines) {
        val (f, s) = l.split(',')
        val (fa, fb) = f.split('-').map { it.toInt() }
        val (sa, sb) = s.split('-').map { it.toInt() }

        if (sa <= fa && sb >= fb) p1++
        else if (fa <= sa && fb >= sb) p1++
        if (minOf(fb, sb) >= maxOf(fa, sa)) p2++
    }

    partOne = p1
    partTwo = p2
}