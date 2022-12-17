package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction.*
import kotlin.math.abs

fun PuzzleSet.day17() = puzzle {
    val instructions = input.map {
        when (it) {
            '<' -> LEFT
            '>' -> RIGHT
            else -> error("Invalid input")
        }
    }

    val rocks = """
            ####

            .#.
            ###
            .#.

            ..#
            ..#
            ###

            #
            #
            #
            #

            ##
            ##
        """.trimIndent().split("\n\n").map { r ->
        val rock = r.lines().asGrid { it == '#' }
        rock.filterTrue().map { (x, y) -> PointL(x + 2L, y - rock.height.toLong()) }
    }

    data class Pattern(val instruction: Int, val piece: Int, val topRows: Set<PointL>)
    data class PatternData(val y: Long, val iteration: Long)

    fun solve(iters: Long): Long {
        val seenPatterns = hashMapOf<Pattern, PatternData>()

        val infPattern = instructions.asSequence().withIndex().repeatInfinitely().iterator()
        val rockPattern = rocks.asSequence().withIndex().repeatInfinitely().iterator()
        val points = hashSetOf<PointL>()

        var iteration = 0L
        var height = 0L
        var repeatedHeight = 0L // part two shenanigans
        while (iteration < iters) {
            val (rockIdx, currentRockPoints) = rockPattern.next()
            var rockPoints = currentRockPoints.map { it.copy(y = it.y + (height - 3L)) }

            while (true) {
                fun tryMove(dir: Direction): Boolean {
                    val new = rockPoints.map { it + dir }
                    val isBlocked = new.any { it in points }
                    val inHeight = new.all { it.y < 0 }
                    val inWidth = new.all { it.x in 0 until 7 }

                    if (!isBlocked && inWidth && inHeight) rockPoints = new
                    return isBlocked || !inHeight
                }

                val (movIdx, movement) = infPattern.next()
                tryMove(movement)
                if (tryMove(DOWN)) {
                    points += rockPoints
                    height = points.minOf { it.y }

                    val topRows = points.filter { (height - it.y) >= -20L }.map { it.copy(y = height - it.y) }.toSet()
                    val pattern = Pattern(movIdx, rockIdx, topRows)
                    seenPatterns[pattern]?.let { seenPtr ->
                        val iterDiff = iteration - seenPtr.iteration
                        val repeat = (iters - iteration) / iterDiff
                        iteration += iterDiff * repeat
                        repeatedHeight += repeat * (height - seenPtr.y)
                    }

                    seenPatterns[pattern] = PatternData(height, iteration)
                    break
                }
            }

            iteration++
        }

        return abs(height + repeatedHeight)
    }

    partOne = solve(2022).s()
    partTwo = solve(1000000000000).s()
}