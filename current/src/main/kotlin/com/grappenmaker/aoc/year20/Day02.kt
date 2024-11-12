@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day02() = puzzle(day = 2) {
    var p1 = 0
    var p2 = 0

    for (line in inputLines) {
        val (rr, l, pwd) = line.split(' ')
        val (a, b) = rr.split('-').map { it.toInt() }

        val sc = l.first()
        if (pwd.countContains(sc) in a..b) p1++

        if ((pwd[a - 1] == sc) xor (pwd[b - 1] == sc)) p2++
    }

    partOne = p1
    partTwo = p2
}