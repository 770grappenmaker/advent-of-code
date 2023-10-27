package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.max

fun PuzzleSet.day8() = puzzle {
    val registers = mutableMapOf<String, Int>()
    fun String.getReg() = registers.getOrPut(this) { 0 }

    // Optimization...
    var maxAllTime = 0
    inputLines.forEach { l ->
        val (insnPart, condition) = l.split(" if ")
        val (target, insn, amount) = insnPart.split(" ")
        val (conditionTarget, conditional, rhs) = condition.split(" ")

        val actualAmount = amount.toInt() * if (insn == "dec") -1 else 1
        val lhs = conditionTarget.getReg()
        val rightParsed = rhs.toInt()
        val matches = when (conditional) {
            ">" -> lhs > rightParsed
            "<" -> lhs < rightParsed
            "<=" -> lhs <= rightParsed
            ">=" -> lhs >= rightParsed
            "==" -> lhs == rightParsed
            "!=" -> lhs != rightParsed
            else -> error("Impossible")
        }

        if (matches) {
            val newValue = actualAmount + target.getReg()
            maxAllTime = max(maxAllTime, newValue)
            registers[target] = newValue
        }
    }

    partOne = registers.values.max().s()
    partTwo = maxAllTime.s()
}