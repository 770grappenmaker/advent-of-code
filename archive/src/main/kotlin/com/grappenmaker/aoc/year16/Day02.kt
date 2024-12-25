package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day2() = puzzle {
    val insns = inputLines.map { it.deepen().map { c -> c.parseDirection().toPoint() } }
    val telephoneGrid = """
        123
        456
        789
    """.trimIndent().lines().asCharGrid()

    val bathroomGrid = """
        ##1##
        #234#
        56789
        #ABC#
        ##D##
    """.trimIndent().lines().asCharGrid()

    fun Grid<Char>.solve(partTwo: Boolean) =
        insns.scan(Point(1, 1)) { acc, curr ->
            curr.fold(acc) { a, c ->
                val newPoint = (a + c).clamp()
                if (partTwo && this[newPoint] == '#') a else newPoint
            }
        }.drop(1).joinToString("") { this[it].toString() }

    partOne = telephoneGrid.solve(false)
    partTwo = bathroomGrid.solve(true)
}