package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.year15.Wiring.*
import com.grappenmaker.aoc.year15.Operator.*

@PuzzleEntry
fun PuzzleSet.day7() = puzzle {
    val insns = inputLines.associate { l ->
        val (operation, target) = l.split(" -> ")
        val parts = operation.split(" ")

        target to when {
            "NOT" in operation -> Not(parts[1].parseSide())
            "AND" in operation -> And(parts[0].parseSide(), parts[2].parseSide())
            "LSHIFT" in operation -> LShift(parts[0].parseSide(), parts[2].parseSide())
            "OR" in operation -> Or(parts[0].parseSide(), parts[2].parseSide())
            "RSHIFT" in operation -> RShift(parts[0].parseSide(), parts[2].parseSide())
            else -> Connect(operation.parseSide())
        }
    }

    val wireA = insns.solve("a")
    partOne = wireA.s()
    partTwo = (insns + ("b" to Connect(Value(wireA)))).solve("a").s()
}

fun Map<String, Operator>.solve(forWire: String) = buildMap {
    fun recurse(node: Wiring): Int = when (node) {
        is Value -> node.literal
        is Wire -> getOrPut(node.id) {
            when (val insn = this@solve[node.id] ?: error("Wire has not yet been resolved?")) {
                is BinaryOperator -> {
                    val lhs = recurse(insn.lhs)
                    val rhs = recurse(insn.rhs)

                    when (insn) {
                        is And -> lhs and rhs
                        is Or -> lhs or rhs
                        is LShift -> lhs shl rhs
                        is RShift -> lhs shr rhs
                    }
                }

                is Not -> recurse(insn.operand).inv()
                is Connect -> recurse(insn.from)
            } and 0xFFFF
        }
    }

    recurse(Wire(forWire))
}.getValue(forWire)

fun String.parseSide(): Wiring = toIntOrNull()?.let { Value(it) } ?: Wire(this)

sealed interface Operator {
    sealed interface BinaryOperator : Operator {
        val lhs: Wiring
        val rhs: Wiring
    }

    data class And(override val lhs: Wiring, override val rhs: Wiring) : BinaryOperator
    data class LShift(override val lhs: Wiring, override val rhs: Wiring) : BinaryOperator
    data class Not(val operand: Wiring) : Operator
    data class Or(override val lhs: Wiring, override val rhs: Wiring) : BinaryOperator
    data class RShift(override val lhs: Wiring, override val rhs: Wiring) : BinaryOperator
    data class Connect(val from: Wiring) : Operator
}

sealed interface Wiring {
    data class Value(val literal: Int) : Wiring
    data class Wire(val id: String) : Wiring
}