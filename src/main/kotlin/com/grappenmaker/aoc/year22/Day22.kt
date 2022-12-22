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

    fun Point.partOneWrap(dir: Direction): Pair<Point, Direction> {
        val next = this + dir
        return when {
            next !in grid || grid[next] == ' ' -> {
                val n = when (dir) {
                    LEFT -> grid.row(y).last { grid[it] != ' ' }
                    RIGHT -> grid.row(y).first { grid[it] != ' ' }
                    UP -> grid.column(x).last { grid[it] != ' ' }
                    DOWN -> grid.column(x).first { grid[it] != ' ' }
                }
                if (grid[n] == '#') this else n
            }

            grid[next] == '#' -> this
            else -> next
        } to dir
    }

    val regionSize = 50
    // "up" face, "right side" face, "front" face, "left side" face, "down" face, "back" face
    // 0          1                  2             3                 4            5
    val regions = listOf(Point(50, 0), Point(100, 0), Point(50, 50), Point(0, 100), Point(50, 100), Point(0, 150))
        .map { Rectangle(it, Point(it.x + regionSize, it.y + regionSize)) }

    fun Point.partTwoWrap(dir: Direction): Pair<Point, Direction> {
        val next = this + dir
        var nextDir = dir
        return when {
            next !in grid || grid[next] == ' ' -> {
//                val (regIdx, reg) = regions.withIndex().first { this in it.value }
                TODO()
            }

            grid[next] == '#' -> this
            else -> next
        } to nextDir
    }

    fun solve(wrapFunc: Point.(Direction) -> Pair<Point, Direction>): String {
        val path = instructions.scan(CurrentLoc()) { (oldDir, oldLoc), (amount, dDir) ->
            var curr = oldLoc
            var currDir = oldDir
            repeat(amount) {
                val res = curr.wrapFunc(currDir)
                curr = res.first
                currDir = res.second
            }

            CurrentLoc(oldDir.next(dDir), curr)
        }

        val last = path.last()
        val dirCode = when (last.dir) {
            RIGHT -> 0
            DOWN -> 1
            LEFT -> 2
            UP -> 3
        }

        val (x, y) = last.loc
        return (((x + 1) * 4) + ((y + 1) * 1000) + dirCode).s()
    }

    partOne = solve(Point::partOneWrap)
    partTwo = solve(Point::partTwoWrap)
}