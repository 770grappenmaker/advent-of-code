package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction.*

fun PuzzleSet.day22() = puzzle {
    data class Instruction(val amount: Int, val direction: Int)

    val (maze, insns) = rawInput.split("\n\n")
    val instructions = buildList {
        var idx = 0
        while (idx in insns.indices) {
            val iPart = insns.substring(idx).substringBefore('R').substringBefore('L')
            val dir = insns.getOrNull(idx + iPart.length)
            add(
                Instruction(
                    iPart.toInt(), when (dir) {
                        'R' -> 1
                        'L' -> -1
                        else -> 0
                    }
                )
            )

            idx += iPart.length + 1
        }
    }

    val mazeLines = maze.lines()
    val width = mazeLines.maxOf { it.length }
    val newLines = mazeLines.map { it.padEnd(width, ' ') }
    val grid = newLines.asGrid { it }

    val start = grid.row(0).first { grid[it] == '.' }

    data class CurrentLoc(val dir: Direction = RIGHT, val loc: Point = start)

    fun partOneWrap(curr: Point, dir: Direction): Pair<Point, Direction> {
        val n = when (dir) {
            LEFT -> grid.row(curr.y).last { grid[it] != ' ' }
            RIGHT -> grid.row(curr.y).first { grid[it] != ' ' }
            UP -> grid.column(curr.x).last { grid[it] != ' ' }
            DOWN -> grid.column(curr.x).first { grid[it] != ' ' }
        }

        return (if (grid[n] == '#') curr else n) to dir
    }

    val regionSize = 50
    val regionM1 = regionSize - 1
    val regions = listOf(Point(1, 0), Point(2, 0), Point(1, 1), Point(1, 2), Point(0, 2), Point(0, 3)).map { (x, y) ->
        val left = Point(x * regionSize, y * regionSize)
        Rectangle(left, left + Point(regionM1, regionM1))
    }

    // POV: you make a paper cube to solve AoC
    val regionsMove = mapOf(
        (0 to UP) to (5 to RIGHT),
        (0 to RIGHT) to (1 to RIGHT),
        (0 to DOWN) to (2 to DOWN),
        (0 to LEFT) to (4 to RIGHT),
        (1 to UP) to (5 to UP),
        (1 to RIGHT) to (3 to LEFT),
        (1 to DOWN) to (2 to LEFT),
        (1 to LEFT) to (0 to LEFT),
        (2 to UP) to (0 to UP),
        (2 to RIGHT) to (1 to UP),
        (2 to DOWN) to (3 to DOWN),
        (2 to LEFT) to (4 to DOWN),
        (3 to UP) to (2 to UP),
        (3 to RIGHT) to (1 to LEFT),
        (3 to DOWN) to (5 to LEFT),
        (3 to LEFT) to (4 to LEFT),
        (4 to UP) to (2 to RIGHT),
        (4 to RIGHT) to (3 to RIGHT),
        (4 to DOWN) to (5 to DOWN),
        (4 to LEFT) to (0 to RIGHT),
        (5 to UP) to (4 to UP),
        (5 to RIGHT) to (3 to UP),
        (5 to DOWN) to (1 to DOWN),
        (5 to LEFT) to (0 to DOWN),
    )

    fun partTwoWrap(curr: Point, dir: Direction): Pair<Point, Direction> {
        val regIdx = regions.findIndexOf { curr in it } ?: error("No reg for point $curr")
        val (newReg, newDir) = regionsMove.getValue(regIdx to dir)
        val localized = curr - regions[regIdx].a

        val coord = when (dir) {
            UP -> localized.x
            DOWN -> regionM1 - localized.x
            LEFT -> regionM1 - localized.y
            RIGHT -> localized.y
        }

        val entering = when (newDir) {
            UP -> Point(coord, regionM1)
            DOWN -> Point(regionM1 - coord, 0)
            LEFT -> Point(regionM1, regionM1 - coord)
            RIGHT -> Point(0, coord)
        }

        val glob = entering + regions[newReg].a
        return if (grid[glob] == '#') curr to dir else glob to newDir
    }

    fun Point.advance(
        dir: Direction,
        wrappingFunc: (Point, Direction) -> Pair<Point, Direction>
    ): Pair<Point, Direction> {
        val next = this + dir
        return when {
            next !in grid || grid[next] == ' ' -> wrappingFunc(this, dir)
            grid[next] == '#' -> this to dir
            else -> next to dir
        }
    }

    fun solve(wrapFunc: (Point, Direction) -> Pair<Point, Direction>): String {
        val last = instructions.fold(CurrentLoc()) { (oldDir, oldLoc), (amount, dDir) ->
            var curr = oldLoc
            var currDir = oldDir

            repeat(amount) {
                val res = curr.advance(currDir, wrapFunc)
                curr = res.first
                currDir = res.second
            }

            CurrentLoc(currDir.next(dDir), curr)
        }

        val dirCode = when (last.dir) {
            RIGHT -> 0
            DOWN -> 1
            LEFT -> 2
            UP -> 3
        }

        val (x, y) = last.loc
        return (((x + 1) * 4) + ((y + 1) * 1000) + dirCode).s()
    }

    partOne = solve(::partOneWrap)
    partTwo = solve(::partTwoWrap)
}