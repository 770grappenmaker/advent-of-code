package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    val (r, c) = input.splitInts()
    var currRow = 1
    var currColumn = 1
    var curr = 20151125L

    while (currRow != r || currColumn != c) {
        curr = (curr * 252533L) % 33554393L

        if (currRow == 1) {
            currRow = currColumn + 1
            currColumn = 1
        } else {
            currRow--
            currColumn++
        }
    }

    partOne = curr.toString()
}