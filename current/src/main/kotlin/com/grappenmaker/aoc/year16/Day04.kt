@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*

fun PuzzleSet.day04() = puzzle(day = 4) {
    var ans = 0

    for (l in inputLines) {
        val name = l.substringBefore('[')
        val check = l.substringAfter('[').dropLast(1).toList()
        val mc = name.filter { it.isLetter() }.toList().frequencies().entries
            .sortedWith(compareByDescending<Map.Entry<Char, Int>> { it.value }.thenBy { it.key })
            .take(5).map { it.key }

        if (mc != check) continue

        val id = l.splitIntsPos().single()
        ans += id

        val words = name.substringBeforeLast('-').split('-')
        val dec = words.joinToString(" ") { w ->
            w.map { c -> 'a' + (((c - 'a') + id) % 26) }.joinToString("") { it.toString() }
        }

        if (dec == "northpole object storage") partTwo = id
    }

    partOne = ans
}