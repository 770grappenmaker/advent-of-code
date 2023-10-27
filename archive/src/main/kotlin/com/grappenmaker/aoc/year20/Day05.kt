package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen

fun PuzzleSet.day5() = puzzle {
    val isTruthy = listOf('B', 'R')
    val ids = inputLines.map { l ->
        l.deepen().asReversed().foldIndexed(0) { idx, acc, c -> acc or ((if (c in isTruthy) 1 else 0) shl idx) }
    }

    partOne = ids.max().s()
    partTwo = (ids.min()..ids.max()).first { it !in ids }.s()
}