package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day17() = puzzle(day = 17) {
    // Increase the recursion limit for this one: -Xss10m
    val lines = inputLines.map { l ->
        val (a, b, c) = l.splitInts()
        if (l.first() == 'x') Point(a, b)..Point(a, c) else Point(b, a)..Point(c, a)
    }

    val originalPoints = lines.flatMap(Line::allPoints)
    val delta = Point(-originalPoints.minX(), 0)
    val points = originalPoints.map { it + delta }

    val grid = points.asBooleanGrid().expandEmpty(x = 1, y = 0)
    val waterSpring = Point(500, 0) + delta
    val generationPoint = waterSpring + DOWN
    val rows = grid.rows

    val settled = mutableSetOf<Point>()
    val flowing = mutableSetOf<Point>()

    fun Point.isCompletelyEmpty() = !grid[this] && this !in settled && this !in flowing

    fun solve(curr: Point = generationPoint) {
        flowing += curr

        val down = curr + DOWN
        if (down !in grid) return

        val left = curr + LEFT
        val right = curr + RIGHT

        if (down.isCompletelyEmpty()) solve(down)
        if (grid[down] || down in settled) {
            if (left in grid && left.isCompletelyEmpty()) solve(left)
            if (right in grid && right.isCompletelyEmpty()) solve(right)
        }

        val (gridLeft, gridRight) = rows[curr.y].splitAt(curr.x)
        fun Iterable<Point>.findWall(): Point? {
            iterator().drain { cur ->
                when {
                    cur !in grid || cur.isCompletelyEmpty() -> return null
                    grid[cur] -> return cur
                }
            }

            return null
        }

        val wallToLeft = gridLeft.asReversed().findWall() ?: return
        val wallToRight = gridRight.findWall() ?: return
        val offset = Point(1, 0)
        val toSettle = (wallToLeft + offset..wallToRight - offset).allPoints().toSet()

        flowing -= toSettle
        settled += toSettle
    }

    solve()
    val my = points.minY()
    fun Set<Point>.result() = count { it.y >= my }.toString()

    partOne = (flowing + settled).result()
    partTwo = settled.result()
}