package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year20.Opcode.*
import com.grappenmaker.aoc.year22.hasDuplicateBy
import com.grappenmaker.aoc.year22.untilNotDistinctBy

fun PuzzleSet.day8() = puzzle(8) {
    val insns = inputLines.map { l ->
        val (a, b) = l.split(" ")
        enumValueOf<Opcode>(a.uppercase()) to b.toInt()
    }

    fun List<Pair<Opcode, Int>>.seq() = generateSequence(0 to 0) { (acc, ptr) ->
        getOrNull(ptr)?.let { (op, a) ->
            when (op) {
                NOP -> acc to ptr + 1
                ACC -> acc + a to ptr + 1
                JMP -> acc to ptr + a
            }
        }
    }

    partOne = insns.seq().untilNotDistinctBy { (_, ptr) -> ptr }.last().first.s()
    partTwo = insns.indices.filter { insns[it].first in listOf(NOP, JMP) }.map { idx ->
        val (op, a) = insns[idx]
        insns.toMutableList().apply { set(idx, (if (op == NOP) JMP else NOP) to a) }.seq()
    }.first { !it.hasDuplicateBy { (_, ptr) -> ptr } }.last().first.s()
}

enum class Opcode {
    NOP, ACC, JMP
}