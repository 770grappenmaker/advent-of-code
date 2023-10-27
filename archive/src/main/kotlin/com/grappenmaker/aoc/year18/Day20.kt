package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*

fun PuzzleSet.day20() = puzzle(day = 20) {
    val toParse = input.drop(1).dropLast(1)

    var ptr = 0
    var curr = Point(0, 0)
    val queue = queueOf<Point>()
    val validDirections = setOf('N', 'E', 'S', 'W')
    val result = mutableMapOf(curr to 0)

    while (ptr < toParse.length) {
        when (toParse[ptr]) {
            in validDirections -> {
                val startPtr = ptr
                while (ptr < toParse.length && toParse[ptr] in validDirections) ptr++

                curr = toParse.substring(startPtr, ptr).trim()
                    .map { enumValues<Direction>()[validDirections.indexOf(it)] }
                    .fold(curr) { a, c ->
                        (a + c).also {
                            result[it] = minOf(result[it] ?: Int.MAX_VALUE, result.getValue(a) + 1)
                        }
                    }

                ptr--
            }

            '|' -> curr = queue.last()
            '(' -> queue.addLast(curr)
            ')' -> queue.removeLast()

            else -> error("Impossible")
        }

        ptr++
    }

    partOne = result.values.max().s()
    partTwo = result.count { (_, b) -> b >= 1000 }.s()
}