package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.permutations

fun PuzzleSet.day7() = puzzle(7) {
    val configurations = (0L..4L).toList().permutations()
    partOne = configurations.maxOf { seq ->
        seq.fold(0L) { acc, curr -> startComputer(input, listOf(curr, acc)).stepUntilOutput() }
    }.s()

    partTwo = configurations.map { l -> l.map { it + 5 } }.maxOf { conf ->
        val pcs = conf.map { mode -> startComputer(input).also { it.addInput(listOf(mode)) } }
        val last = pcs.last()

        generateSequence(0L) { curr ->
            pcs.fold(curr) { acc, pc ->
                pc.addInput(listOf(acc))
                pc.stepUntilOutput()
            }
        }.takeWhile { !last.isHalted() }.last()
    }.s()
}
