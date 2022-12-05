package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.test.Instruction.*
import com.grappenmaker.aoc.year22.*

fun main() = simplePuzzle(8, 2016) {
    val insns = inputLines.map { l ->
        val split = l.split(" ")
        when (split[0]) {
            "rotate" -> {
                val by = split.last().toInt()
                val rc = split[2].substringAfter("=").toInt()
                when (split[1]) {
                    "row" -> RotateRow(rc, by)
                    "column" -> RotateColumn(rc, by)
                    else -> error("Impossible")
                }
            }

            "rect" -> {
                val (w, h) = split[1].split("x").map(String::toInt)
                Rect(w, h)
            }

            else -> error("Impossible")
        }
    }

    with(mutableBooleanGrid(50, 6)) {
        insns.forEach { insn ->
            when (insn) {
                is Rect -> insn.rect.points.forEach { enable(it) }
                is RotateColumn -> rotateColumn(insn.idx, insn.by)
                is RotateRow -> rotateRow(insn.idx, insn.by)
            }
        }

        partOne = countTrue().s()
        partTwo = "\n" + debug()
    }
}

val Rect.rect get() = sizedRect(width, height)

sealed interface Instruction {
    data class Rect(val width: Int, val height: Int) : Instruction

    sealed interface Rotate : Instruction {
        val idx: Int
        val by: Int
    }

    data class RotateRow(override val idx: Int, override val by: Int) : Rotate
    data class RotateColumn(override val idx: Int, override val by: Int) : Rotate
}