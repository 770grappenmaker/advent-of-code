package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import java.util.*

fun PuzzleSet.day21() = puzzle(day = 21) {
    fun String.scramble(): String {
        val curr = toMutableList()
        inputLines.forEach { l ->
            val parts = l.split(" ")
            val ints = l.splitInts()

            when (parts.first()) {
                "swap" -> when (parts[1]) {
                    "position" -> Collections.swap(curr, ints[0], ints[1])
                    "letter" -> Collections.swap(curr, curr.indexOf(parts[2].single()), curr.indexOf(parts[5].single()))
                }

                "rotate" -> curr.rotateInPlace(
                    when (parts[1]) {
                        "left" -> -ints.first()
                        "right" -> ints.first()
                        "based" -> {
                            val idx = curr.indexOf(parts.last().single())
                            idx + 1 + if (idx >= 4) 1 else 0
                        }
                        else -> error("Impossible")
                    }
                )

                "reverse" -> curr.addAll(ints[0], curr.remove(ints[0]..ints[1]).asReversed())
                "move" -> curr.add(ints[1], curr.removeAt(ints[0]))
            }
        }

        return curr.joinToString("")
    }

    val original = "fbgdceah"
    partOne = "abcdefgh".scramble()
    partTwo = original.toList().permutations().first { it.joinToString("").scramble() == original }.joinToString("")
}