package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

// Dirty but gets the job done. JS folks are probably using eval() anyway
fun PuzzleSet.day8() = puzzle {
    val codeSize = inputLines.sumOf { it.length }
    val memSize = inputLines.sumOf { l ->
        l
            .trim('"')
            .replace("\\\\", "1")
            .replace("\\\"", "1")
            .replace("\\\\x[a-fA-F0-9]{2}".toRegex(), "1").length
    }

    partOne = (codeSize - memSize).s()

    val escapedSize = inputLines.sumOf { l ->
        2 + l.replace("\\", "\\\\").replace("\"", "\\\"").length
    }

    partTwo = (escapedSize - codeSize).s()
}