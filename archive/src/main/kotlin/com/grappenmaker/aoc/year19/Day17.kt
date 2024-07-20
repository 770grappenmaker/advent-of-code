package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day17() = puzzle(17) {
    val prog = input.split(",").map(String::toLong)
    val grid = prog.programResults()
        .joinToString("") { it.toInt().toChar().toString() }.trim().lines().asCharGrid()

    with(grid) {
        partOne = points.filter { p ->
            this[p] == '#' && p.adjacentSides().all { this[it] == '#' }
        }.sumOf { (x, y) -> x * y }.s()

        val dirs = listOf('^', '>', 'v', '<').zip(enumValues<Direction>()).toMap()
        val starting = points.single { this[it] in dirs }
        val startingDir = dirs.getValue(this[starting])

        data class Node(val loc: Point, val dir: Direction, val dirChange: Int, val amount: Int)

        fun Point.isValid() = this@with[this] != '.'

        val path = generateSequence(Node(starting, startingDir, 0, 0)) { (curr, currDir) ->
            listOf(-1, 1).mapNotNull { dd ->
                val dir = currDir.next(dd)
                generateSequence(curr) { it + dir }.takeWhile { it in this && it.isValid() }.lastOrNull()
                    ?.let { p -> Node(p, dir, dd, p manhattanDistanceTo curr) }
            }.maxByOrNull { it.amount }
        }.drop(1).takeUntil { it.loc.adjacentSides().count { p -> p.isValid() } > 1 }.toList()

        // Regex magic by u/Sephibro
        // https://www.reddit.com/r/adventofcode/comments/ebr7dg/comment/fb7ymcw/?utm_source=share&utm_medium=web2x&context=3
        val pathCommand = path.joinToString(",") { "${if (it.dirChange == -1) "L" else "R"},${it.amount}" }
        val joined = "$pathCommand,"
        val regex = """^(.{1,21})\1*(.{1,21})(?:\1|\2)*(.{1,21})(?:\1|\2|\3)*$""".toRegex()
        val (a, b, c) = regex.matchEntire(joined)!!.groupValues.drop(1).map { it.removeSuffix(",") }
        val seq = pathCommand.replace(a, "A").replace(b, "B").replace(c, "C")
        partTwo = (listOf(2L) + prog.drop(1)).evalProgram(
            (listOf(seq, a, b, c, "n").joinToString("\n") + "\n").map { it.code.toLong() }).s()
    }

    // Why manually? IDK
//    val commands = (listOf(
//        "A,B,A,C,A,A,C,B,C,B",
//        "L,12,L,8,R,12",
//        "L,10,L,8,L,12,R,12",
//        "R,12,L,8,L,10",
//        "n"
//    ).joinToString("\n") + "\n").map { it.code.toLong() }
//    partTwo = (listOf(2L) + prog.drop(1)).evalProgram(commands).s()
}