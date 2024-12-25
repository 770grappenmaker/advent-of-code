package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day16() = puzzle(day = 16) {
    val (examplePart, sampleProgramPart) = input.split("\n\n\n")

    data class ExampleEntry(val before: List<Int>, val opcode: Int, val args: List<Int>, val after: List<Int>)
    fun ExampleEntry.match(op: Opcode, poss: List<Boolean>): Boolean {
        val state = before.toMutableList()
        op.exec(state, args, poss)
        return state == after
    }

    val parsedExample = examplePart.split("\n\n").map { p ->
        val (before, insn, after) = p.lines().map { it.splitInts() }
        ExampleEntry(before, insn.first(), insn.drop(1), after)
    }

    partOne = parsedExample.count { example ->
        allOpcodes.sumOf { op -> op.argumentArrangements.count { example.match(op, it) } } >= 3
    }.toString()

    val exampleByCode = parsedExample.groupBy { it.opcode }
    val allCallMethods = allOpcodes.flatMap { op -> op.argumentArrangements.map { op to it } }

    // Horrible data structure, but it is fine... right??
    fun recurse(
        total: List<Pair<Opcode, List<Boolean>>> = emptyList(),
        methods: List<Pair<Opcode, List<Boolean>>> = allCallMethods,
    ): List<Pair<Opcode, List<Boolean>>>? {
        if (methods.isEmpty()) return total
        return methods.filter { (op, poss) ->
            exampleByCode.getValue(total.size).all { example -> example.match(op, poss) }
        }.mapNotNull { n -> recurse(total + n, methods - n) }.singleOrNull()
    }

    val opcodeLookup = (recurse() ?: error("No unambiguous combo"))
        .withIndex().associateBy { it.index }.mapValues { (_, v) -> v.value }

    val state = MutableList(4) { 0 }
    sampleProgramPart.trim().lines().map { it.splitInts() }.forEach { insn ->
        val (op, poss) = opcodeLookup.getValue(insn.first())
        op.exec(state, insn.drop(1), poss)
    }

    partTwo = state[0].toString()
}