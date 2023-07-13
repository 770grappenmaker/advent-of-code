package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.splitInts

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
    }.s()

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

    partTwo = state[0].s()
}

typealias RegisterContainer = MutableList<Int>

sealed interface OpcodeValue {
    operator fun get(registers: RegisterContainer): Int
    operator fun set(registers: RegisterContainer, value: Int)
}

data class RegisterValue(val index: Int) : OpcodeValue {
    override fun get(registers: RegisterContainer) = registers[index]
    override fun set(registers: RegisterContainer, value: Int) {
        registers[index] = value
    }
}

data class ImmediateValue(val value: Int) : OpcodeValue {
    override fun get(registers: RegisterContainer) = value
    override fun set(registers: RegisterContainer, value: Int) = error("Cannot set immediate value ($value)")
}

val allOpcodes = listOf(Addition, Multiplication, BitwiseAND, BitwiseOR, Assignment, GTTesting, EQTesting)

sealed interface Opcode {
    // Boolean represents if immediate
    val argumentArrangements: List<List<Boolean>> get() = listOf(listOf(false, false), listOf(false, true))
    fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue)
}

fun List<Int>.decodeArguments(arrangement: List<Boolean>) =
    (arrangement + false).zip(this).map { (i, v) -> if (i) ImmediateValue(v) else RegisterValue(v) }

fun Opcode.exec(registers: RegisterContainer, nums: List<Int>, arrangement: List<Boolean>) {
    val (a, b, c) = nums.decodeArguments(arrangement)
    exec(registers, a, b, c)
}

object Addition : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] + b[registers]
    }
}

object Multiplication : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] * b[registers]
    }
}

object BitwiseAND : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] and b[registers]
    }
}

object BitwiseOR : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] or b[registers]
    }
}

object Assignment : Opcode {
    override val argumentArrangements = listOf(listOf(false, false), listOf(true, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers]
    }
}

object GTTesting : Opcode {
    override val argumentArrangements = listOf(listOf(true, false), listOf(false, true), listOf(false, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = if (a[registers] > b[registers]) 1 else 0
    }
}

object EQTesting : Opcode {
    override val argumentArrangements = listOf(listOf(true, false), listOf(false, true), listOf(false, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = if (a[registers] == b[registers]) 1 else 0
    }
}