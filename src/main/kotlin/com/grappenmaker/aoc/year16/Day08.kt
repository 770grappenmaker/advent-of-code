package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.*

fun PuzzleSet.day8() = puzzle {
    val grid = buildBooleanGrid(50, 6) {
        inputLines.forEach { l ->
            val split = l.split(" ")
            when (split[0]) {
                "rotate" -> {
                    val by = split.last().toInt()
                    val rc = split[2].substringAfter("=").toInt()
                    when (split[1]) {
                        "row" -> rotateRow(rc, by)
                        "column" -> rotateColumn(rc, by)
                        else -> error("Impossible")
                    }
                }

                "rect" -> {
                    val (w, h) = split[1].split("x").map(String::toInt)
                    sizedRect(w, h).points.forEach { enable(it) }
                }

                else -> error("Impossible")
            }
        }
    }

    partOne = grid.countTrue().s()
    partTwo = "\n" + grid.debug(on = "++ ", off = "   ")
}