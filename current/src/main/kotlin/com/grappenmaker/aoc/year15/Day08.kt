@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.*
import kotlin.math.*
import java.util.PriorityQueue

fun PuzzleSet.day08() = puzzle(day = 8) {
    var code = 0
    var chars = 0
    var ex = 0

    for (l in inputLines) {
        var idx = 0

        while (idx in l.indices) {
            when (l[idx++]) {
                '\\' -> when (l[idx++]) {
                    '"', '\\' -> {
                        ex += 3
                        code++
                    }

                    'x' -> {
                        ex += 4
                        code += 3
                        idx += 2
                    }
                }

                '"' -> {
                    chars--
                    ex += 2
                }
            }

            ex++
            code++
            chars++
        }
    }

    partOne = code - chars
    partTwo = ex - code
}