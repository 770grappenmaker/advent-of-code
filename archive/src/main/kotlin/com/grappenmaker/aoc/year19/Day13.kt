package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.Point
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.sign

@PuzzleEntry
fun PuzzleSet.day13() = puzzle(13) {
    val drawn = input.programResults().map(Long::toInt).chunked(3).map { (x, y, t) -> Point(x, y) to t }
    partOne = drawn.count { (_, t) -> t == 2 }.toString()

    with(startComputer(listOf(2L) + input.split(",").map(String::toLong).drop(1))) {
        val tiles = hashMapOf<Point, Long>()
        var score = 0L
        fun find(t: Long) = tiles.toList().first { (_, v) -> v == t }.first

        input {
            // Uncomment for visualization
//            val width = tiles.keys.maxOf { it.x } + 1
//            val height = tiles.keys.maxOf { it.y } + 1

//            println(grid(width, height) {
//                when (tiles[it]) {
//                    0L -> ' '
//                    1L -> '#'
//                    2L -> '+'
//                    3L -> '_'
//                    4L -> '*'
//                    else -> error("OOPS")
//                }
//            }.debug())
//            println("Score: $score")
//
//            Thread.sleep(50)

            (find(4).x - find(3).x).sign.toLong()
        }

        outputSequence().chunked(3).filter { it.size == 3 }.forEach { (x, y, t) ->
            when {
                x == -1L && y == 0L -> score = t
                else -> tiles[Point(x.toInt(), y.toInt())] = t
            }
        }

        partTwo = score.toString()
    }
}